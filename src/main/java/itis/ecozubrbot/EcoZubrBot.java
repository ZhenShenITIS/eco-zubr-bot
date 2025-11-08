package itis.ecozubrbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.max.bot.annotations.UpdateHandler;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.longpolling.LongPollingBot;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
public class EcoZubrBot extends LongPollingBot {

    public EcoZubrBot(String accessToken) {
        super(accessToken);
    }

    @UpdateHandler
    public void onMessageCreated(MessageCreatedUpdate update) throws ClientException {
        Message message = update.getMessage();
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText("Эхо: " + message.getBody().getText()).build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(getClient(), replyMessage).chatId(chatId);
        query.enqueue();
    }




}
