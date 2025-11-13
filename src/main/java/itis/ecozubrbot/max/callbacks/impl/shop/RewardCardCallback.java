package itis.ecozubrbot.max.callbacks.impl.shop;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Reward;
import itis.ecozubrbot.services.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.EditMessageQuery;

@Component
@AllArgsConstructor
public class RewardCardCallback implements Callback {
    private final CallbackName callbackName = CallbackName.REWARD_CARD;
    private final RewardService rewardService;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long rewardId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[2]);
        Reward reward = rewardService.getById(rewardId);
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(reward.toString())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.singleColumn(
                                new CallbackButton(
                                        CallbackName.REWARD_PURCHASE.getCallbackName() + ":" + rewardId,
                                        StringConstants.REWARD_PURCHASE_BUTTON.getValue()),
                                new CallbackButton(
                                        CallbackName.SHOP.getCallbackName() + ":" + nextIndex,
                                        StringConstants.BACK_BUTTON.getValue())))
                        .with(AttachmentsBuilder.photos(reward.getImageUrl())))
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
