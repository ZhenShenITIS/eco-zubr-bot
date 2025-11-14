package itis.ecozubrbot.max.callbacks.impl.event;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.MessageHelper;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.services.EventService;
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
public class EventCardCallback implements Callback {
    private final CallbackName callbackName = CallbackName.EVENT_CARD;
    private final EventService eventService;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long challengeId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[2]);
        Event event = eventService.getById(challengeId);
        NewMessageBody replyMessage = MessageHelper.getNewMessageBody(
                event.toString(),
                InlineKeyboardBuilder.singleColumn(
                        new CallbackButton(
                                CallbackName.EVENT_DONE.getCallbackName() + ":" + challengeId,
                                StringConstants.EVENT_DONE_BUTTON.getValue()),
                        new CallbackButton(
                                CallbackName.EVENTS.getCallbackName() + ":" + nextIndex,
                                StringConstants.BACK_BUTTON.getValue())),
                event.getImageUrl());
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
