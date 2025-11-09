package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.max.callbacks.Callback;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
public class TestCallback implements Callback {
    private final CallbackName callbackName = CallbackName.TEST;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        NewMessageBody replyMessage =
                NewMessageBodyBuilder.ofText("Ответ на нажатие кнопки").build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
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
