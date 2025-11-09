package itis.ecozubrbot.max.commands;

import itis.ecozubrbot.constants.CommandName;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCreatedUpdate;

public interface Command {
    CommandName getCommand();

    void handleCommand(MessageCreatedUpdate update, MaxClient client);
}
