package itis.ecozubrbot.max.handlers;

import itis.ecozubrbot.max.containers.CallbackContainer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

@AllArgsConstructor
@Component
public class MessageCallbackHandler {

    private CallbackContainer callbackContainer;

    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        callbackContainer.retrieveCallback(update.getCallback().getPayload()).handleMessageCallback(update, client);
    }
}
