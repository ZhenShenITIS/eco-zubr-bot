package itis.ecozubrbot.max.callbacks.impl.challenge;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.MessageHelper;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.services.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.EditMessageQuery;

@Component
@AllArgsConstructor
public class ChallengeCardCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CHALLENGE_CARD;
    private ChallengeService challengeService;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long challengeId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[2]);
        Challenge challenge = challengeService.getById(challengeId);

        NewMessageBody replyMessage = MessageHelper.getNewMessageBody(
                challenge.toString(),
                InlineKeyboardBuilder.singleColumn(
                        new CallbackButton(
                                CallbackName.CHALLENGE_DONE.getCallbackName() + ":" + challengeId,
                                StringConstants.CHALLENGE_DONE_BUTTON.getValue()),
                        new CallbackButton(
                                CallbackName.CHALLENGES.getCallbackName() + ":" + nextIndex,
                                StringConstants.BACK_BUTTON.getValue())),
                challenge.getImageUrl());
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
