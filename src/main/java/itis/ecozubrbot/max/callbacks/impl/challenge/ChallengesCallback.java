package itis.ecozubrbot.max.callbacks.impl.challenge;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.IntegerConstants;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.services.ChallengeService;
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
public class ChallengesCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CHALLENGES;

    private ChallengeService challengeService;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[1]);
        nextIndex = Math.max(nextIndex, 0);
        List<Challenge> challenges = challengeService.getChallengesSortedByPoints();
        List<List<Button>> layout = new ArrayList<>();
        int countOfIncrease = 0;
        int i;
        for (i = nextIndex;
                i
                        < nextIndex
                                + Math.min(
                                        IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue(),
                                        challenges.size() - nextIndex);
                i++, countOfIncrease++) {
            List<Button> row = new ArrayList<>();
            Challenge challenge = challenges.get(i);
            row.add(new CallbackButton(
                    CallbackName.CHALLENGE_CARD.getCallbackName() + ":" + challenge.getId() + ":" + nextIndex,
                    challenge.getTitle() + " | " + challenge.getPointsReward() + "/"
                            + challenge.getExperienceReward()));
            layout.add(row);
        }
        List<Button> arrowRow = new ArrayList<>();
        arrowRow.add(new CallbackButton(
                CallbackName.CHALLENGES.getCallbackName() + ":"
                        + (i - IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue() - countOfIncrease),
                StringConstants.BACKWARD_LIST_BUTTON.getValue()));
        arrowRow.add(new CallbackButton("empty", StringConstants.VOID.getValue()));
        arrowRow.add(new CallbackButton(
                CallbackName.CHALLENGES.getCallbackName() + ":" + i, StringConstants.FORWARD_LIST_BUTTON.getValue()));
        layout.add(arrowRow);
        List<Button> backButton = List.of(new CallbackButton(
                CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue()));
        layout.add(backButton);

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText("Здесь раздел с заданиями")
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(layout)))
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
