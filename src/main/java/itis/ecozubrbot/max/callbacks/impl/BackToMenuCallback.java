package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.commands.impl.MenuCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;

@Component
@AllArgsConstructor
public class BackToMenuCallback implements Callback {

    private MenuCommand menuCommand;
    private final CallbackName callbackName = CallbackName.BACK_TO_MENU;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        MessageCreatedUpdate messageCreatedUpdate =
                new MessageCreatedUpdate(update.getMessage(), update.getTimestamp());
        menuCommand.handleCommand(messageCreatedUpdate, client);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
