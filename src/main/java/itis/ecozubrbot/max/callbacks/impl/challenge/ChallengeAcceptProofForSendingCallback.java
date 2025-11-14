package itis.ecozubrbot.max.callbacks.impl.challenge;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.MessageHelper;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserChallengeOnModerationRepository;
import itis.ecozubrbot.services.UserChallengeService;
import itis.ecozubrbot.services.newsletterwithtimer.ModerationChallengeFirstService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class ChallengeAcceptProofForSendingCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CHALLENGE_ACCEPT_FOR_SENDING_PROOF;
    private ModerationChallengeFirstService moderationChallengeFirstService;
    private UserChallengeOnModerationRepository userChallengeOnModerationRepository;
    private UserChallengeService userChallengeService;
    private StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long userId = update.getCallback().getUser().getUserId();
        moderationChallengeFirstService.createModeration(
                userChallengeService.getById(userChallengeOnModerationRepository.getUserChallengeId(userId)), client);

        userChallengeOnModerationRepository.remove(userId);
        NewMessageBody replyMessage = MessageHelper.getNewMessageBody(
                StringConstants.PROOF_GET_SUCCESS.getValue(),
                InlineKeyboardBuilder.single(new CallbackButton(
                        CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue())),
                null);
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        stateRepository.put(userId, StateName.DEFAULT);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
