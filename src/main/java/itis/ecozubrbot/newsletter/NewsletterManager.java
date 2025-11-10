package itis.ecozubrbot.newsletter;

import java.util.List;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

public class NewsletterManager {
    private final MaxClient client;

    public NewsletterManager(MaxClient client) {
        this.client = client;
    }

    public void sendMessage(NewMessageBody messageBody, Long chatId) {
        SendMessageQuery query = new SendMessageQuery(client, messageBody).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNewsletter(List<Long> chats_id, NewMessageBody newMessageBody) {
        for (Long chat_id : chats_id) {
            sendMessage(newMessageBody, chat_id);
        }
    }
}
