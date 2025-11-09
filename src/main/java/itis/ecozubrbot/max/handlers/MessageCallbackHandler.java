package itis.ecozubrbot.max.handlers;

import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

@Component
public class MessageCallbackHandler {
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}
}
