package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.max.callbacks.Callback;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

@Component
@AllArgsConstructor
public class EmptyCallback implements Callback {
    private final CallbackName callbackName = CallbackName.EMPTY;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
