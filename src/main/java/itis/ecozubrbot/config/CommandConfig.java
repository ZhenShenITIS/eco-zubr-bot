package itis.ecozubrbot.config;

import itis.ecozubrbot.max.commands.Command;
import itis.ecozubrbot.max.commands.impl.HelpCommand;
import itis.ecozubrbot.max.containers.CommandContainer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@AllArgsConstructor
@Configuration
public class CommandConfig {
    private HelpCommand helpCommand;
    private List<Command> commandList;
    @Bean
    public CommandContainer commandContainer() {
        commandList.add(helpCommand);
        return new CommandContainer(commandList);
    }
}
