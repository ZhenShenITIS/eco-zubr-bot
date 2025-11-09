package itis.ecozubrbot.max.callbacks;

import itis.ecozubrbot.constants.CallbackName;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

public interface Callback {
    void handleMessageCallback(MessageCallbackUpdate update, MaxClient client);

    CallbackName getCallback();
}
