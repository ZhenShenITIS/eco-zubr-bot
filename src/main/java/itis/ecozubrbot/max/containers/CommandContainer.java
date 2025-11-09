package itis.ecozubrbot.max.containers;

import com.google.common.collect.ImmutableMap;
import itis.ecozubrbot.max.commands.Command;
import itis.ecozubrbot.max.commands.impl.UnknownCommand;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class CommandContainer {
    private final ImmutableMap<String, Command> commands;
    private final Command unknownCommand;

    public CommandContainer(List<Command> commandList) {
        HashMap<String, Command> map = new HashMap<>();
        for (Command command : commandList) {
            map.put(command.getCommand().getCommandName(), command);
        }
        commands = ImmutableMap.copyOf(map);
        unknownCommand = new UnknownCommand();
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commands.getOrDefault(commandIdentifier, unknownCommand);
    }
}
