package itis.ecozubrbot.max.states;

import itis.ecozubrbot.constants.StateName;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;

public interface State {
    void handleMessageCallback(MessageCallbackUpdate update, MaxClient client);

    void handleMessageCreated(MessageCreatedUpdate update, MaxClient client);

    StateName getState();
}
