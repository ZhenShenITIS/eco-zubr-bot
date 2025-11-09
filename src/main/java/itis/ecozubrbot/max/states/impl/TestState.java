package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.max.states.State;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.User;
import ru.max.botapi.queries.SendMessageQuery;

@Component
public class TestState implements State {

    private final StateName state = StateName.TEST;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        Message message = update.getMessage();
        User user = message.getSender();
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText("Информация о вашем профиле: \n"
                        + "FirstName: " + user.getFirstName() + "\n"
                        + "LastName: " + user.getLastName() + "\n"
                        + "Name: " + user.getName() + "\n"
                        + "UserId: " + user.getUserId() + "\n"
                        + "Username: " + user.getUsername())
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StateName getState() {
        return state;
    }
}
