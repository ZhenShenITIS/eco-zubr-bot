package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.max.handlers.MessageCallbackHandler;
import itis.ecozubrbot.max.handlers.MessageCreatedHandler;
import itis.ecozubrbot.max.states.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;

@AllArgsConstructor
@Component
public class DefaultState implements State {
    private final StateName state = StateName.DEFAULT;

    private MessageCallbackHandler messageCallbackHandler;
    private MessageCreatedHandler messageCreatedHandler;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        messageCallbackHandler.handleMessageCallback(update, client);
    }

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        messageCreatedHandler.handleMessageCreated(update, client);
    }

    @Override
    public StateName getState() {
        return state;
    }
}
