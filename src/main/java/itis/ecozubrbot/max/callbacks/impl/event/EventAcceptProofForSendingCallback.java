package itis.ecozubrbot.max.callbacks.impl.event;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.MessageHelper;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserEventOnModerationRepository;
import itis.ecozubrbot.services.UserEventService;
import itis.ecozubrbot.services.newsletterwithtimer.ModerationEventFirstService;
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
}
