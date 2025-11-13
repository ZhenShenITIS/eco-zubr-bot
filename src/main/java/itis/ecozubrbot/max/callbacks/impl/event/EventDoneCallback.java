package itis.ecozubrbot.max.callbacks.impl.event;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserEventOnModerationRepository;
import itis.ecozubrbot.services.EventService;
import itis.ecozubrbot.services.UserEventService;
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
public class EventDoneCallback implements Callback {
    private final CallbackName callbackName = CallbackName.EVENT_DONE;
    private final UserEventService userEventService;
    private final UserService userService;
    private final EventService eventService;
    private final UserEventOnModerationRepository userEventOnModerationRepository;
    private final StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.WAITING_OF_PROOFS.getValue())
                .build();
        Long eventId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        Long chatId = update.getMessage().getRecipient().getChatId();
        Long userId = update.getCallback().getUser().getUserId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        Long userEventId = userEventService.createAndReturnId(
                userService.getById(userId).getId(),
                eventService.getById(eventId).getId(),
                TaskStatus.UNDER_REVIEW);
        userEventOnModerationRepository.put(userId, userEventId);
        stateRepository.put(userId, StateName.WAITING_PROOF_OF_EVENT);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
