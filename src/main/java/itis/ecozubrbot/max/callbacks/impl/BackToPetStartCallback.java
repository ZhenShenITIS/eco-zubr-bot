package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.max.callbacks.Callback;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

@Component
@AllArgsConstructor
public class BackToPetStartCallback implements Callback {
    private final CallbackName callbackName = CallbackName.BACK_TO_PET_START;
    private PetStartCallback petStartCallback;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        petStartCallback.handleMessageCallback(update, client);
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
