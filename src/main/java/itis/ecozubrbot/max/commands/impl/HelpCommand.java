package itis.ecozubrbot.max.commands.impl;

import itis.ecozubrbot.constants.CommandName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.commands.Command;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
public class HelpCommand implements Command {
    private final CommandName commandName = CommandName.HELP;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public void handleCommand(MessageCreatedUpdate update, MaxClient client) {
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.HELP.getValue())
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
