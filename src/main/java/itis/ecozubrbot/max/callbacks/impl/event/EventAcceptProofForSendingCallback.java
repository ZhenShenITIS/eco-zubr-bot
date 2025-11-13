package itis.ecozubrbot.max.callbacks.impl.event;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserEventOnModerationRepository;
import itis.ecozubrbot.service.newsletterwithtimer.ModerationEventFirstService;
import itis.ecozubrbot.services.UserEventService;
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
public class EventAcceptProofForSendingCallback implements Callback {
    private final CallbackName callbackName = CallbackName.EVENT_ACCEPT_FOR_SENDING_PROOF;
    private final UserEventService userEventService;
    private final UserEventOnModerationRepository userEventOnModerationRepository;
    private StateRepository stateRepository;
    private ModerationEventFirstService moderationEventFirstService;

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long userId = update.getCallback().getUser().getUserId();
        moderationEventFirstService.createModeration(
                userEventService.getById(userEventOnModerationRepository.getUserEventId(userId)), client);
        userEventOnModerationRepository.remove(userId);
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.PROOF_GET_SUCCESS.getValue())
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        stateRepository.put(userId, StateName.DEFAULT);
    }
}
