package itis.ecozubrbot.max.handlers;

import itis.ecozubrbot.max.containers.CommandContainer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCreatedUpdate;

@AllArgsConstructor
@Component
public class MessageCreatedHandler {

    private CommandContainer commandContainer;

    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        String messageText = update.getMessage().getBody().getText().trim();
        if (messageText.startsWith("/")) {
            commandContainer.retrieveCommand(messageText).handleCommand(update, client);
        }
    }
}
