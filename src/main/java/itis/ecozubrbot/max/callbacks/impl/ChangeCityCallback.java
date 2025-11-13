package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.repositories.StateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.RequestGeoLocationButton;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class ChangeCityCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CHANGE_CITY;
    private StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.CHANGE_CITY_INFO.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(
                        new RequestGeoLocationButton(StringConstants.GEOLOCATION_BUTTON.getValue()))))
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
            stateRepository.put(update.getCallback().getUser().getUserId(), StateName.GEOLOCATION);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
