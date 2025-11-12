package itis.ecozubrbot.max.callbacks.impl.challenge;
// У моего UserChallenge поля userId и challengeId логически являются ключом. Как я могу достать объект UserChallenge

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserChallengeOnModerationRepository;
import itis.ecozubrbot.services.ChallengeService;
import itis.ecozubrbot.services.UserChallengeService;
import itis.ecozubrbot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class ChallengeDoneCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CHALLENGE_DONE;
    private StateRepository stateRepository;
    private UserChallengeOnModerationRepository userChallengeOnModerationRepository;
    private UserChallengeService userChallengeService;
    private UserService userService;
    private ChallengeService challengeService;

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.WAITING_OF_PROOFS.getValue())
                .build();
        Long challengeId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        Long chatId = update.getMessage().getRecipient().getChatId();
        Long userId = update.getCallback().getUser().getUserId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        Long userChallengeId = userChallengeService.createAndReturnId(
                userService.getById(userId).getId(),
                challengeService.getById(challengeId).getId(),
                TaskStatus.UNDER_REVIEW);
        userChallengeOnModerationRepository.put(userId, userChallengeId);
        stateRepository.put(userId, StateName.WAITING_PROOF_OF_CHALLENGE);
    }
}
