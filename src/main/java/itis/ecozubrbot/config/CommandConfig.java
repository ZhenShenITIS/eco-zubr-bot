package itis.ecozubrbot.config;

import itis.ecozubrbot.max.commands.Command;
import itis.ecozubrbot.max.commands.impl.AddContentCommand;
import itis.ecozubrbot.max.commands.impl.HelpCommand;
import itis.ecozubrbot.max.commands.impl.MenuCommand;
import itis.ecozubrbot.max.containers.CommandContainer;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class CommandConfig {
    private HelpCommand helpCommand;
    private MenuCommand menuCommand;
    private AddContentCommand addContentCommand;
    private List<Command> commandList;

    @Bean
    public CommandContainer commandContainer() {
        commandList.add(helpCommand);
        commandList.add(menuCommand);
        commandList.add(addContentCommand);
        return new CommandContainer(commandList);
    }
}
