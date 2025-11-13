package itis.ecozubrbot.max.callbacks.impl.shop;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.IntegerConstants;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.models.Reward;
import itis.ecozubrbot.services.RewardService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.EditMessageQuery;

@Component
@AllArgsConstructor
public class ShopCallback implements Callback {
    private final CallbackName callbackName = CallbackName.SHOP;
    private final RewardService rewardService;
    private BasicFileMap basicFileMap;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[1]);
        nextIndex = Math.max(nextIndex, 0);
        List<Reward> rewards = rewardService.getRewardsSortedByPoints();

        List<List<Button>> layout = new ArrayList<>();
        int countOfIncrease = 0;
        int i;
        int bounds =
                nextIndex + Math.min(IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue(), rewards.size() - nextIndex);
        for (i = nextIndex; i < bounds; i++, countOfIncrease++) {
            List<Button> row = new ArrayList<>();
            Reward reward = rewards.get(i);

            String text = reward.getTitle() + " | " + reward.getPointsCost() + "/" + reward.getAvailableQuantity();
            int maxLen = IntegerConstants.MAX_LENGTH_TEXT_OF_LIST_BUTTON.getValue();
            if (text.length() > maxLen) {
                int indexFin = reward.getTitle().length() - 4 - (text.length() - maxLen);
                indexFin = Math.max(indexFin, 0);
                text = reward.getTitle().substring(0, indexFin) + "... | " + reward.getPointsCost() + "/"
                        + reward.getAvailableQuantity();
            }

            row.add(new CallbackButton(
                    CallbackName.REWARD_CARD.getCallbackName() + ":" + reward.getId() + ":" + nextIndex, text));
            layout.add(row);
        }
        List<Button> arrowRow = new ArrayList<>();
        arrowRow.add(new CallbackButton(
                CallbackName.SHOP.getCallbackName() + ":"
                        + (i - IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue() - countOfIncrease),
                StringConstants.BACKWARD_LIST_BUTTON.getValue()));

        arrowRow.add(new CallbackButton(CallbackName.EMPTY.getCallbackName(), StringConstants.VOID.getValue()));

        CallbackName forwardCallback = (i == rewards.size()) ? CallbackName.EMPTY : callbackName.SHOP;

        arrowRow.add(new CallbackButton(
                forwardCallback.getCallbackName() + ":" + i, StringConstants.FORWARD_LIST_BUTTON.getValue()));
        layout.add(arrowRow);
        List<Button> backButton = List.of(new CallbackButton(
                CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue()));
        layout.add(backButton);

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.SHOP.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(layout))
                        .with(AttachmentsBuilder.photos(basicFileMap.getToken(BasicFile.SHOP))))
                .build();
        EditMessageQuery query = new EditMessageQuery(
                client, replyMessage, update.getMessage().getBody().getMid());
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
