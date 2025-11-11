package itis.ecozubrbot.max.commands.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.CommandName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.commands.Command;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class AddChallengeCommand implements Command {
    private final CommandName commandName = CommandName.ADD_CHALLENGE;
    private StateRepository stateRepository;
    private UserService userService;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public void handleCommand(MessageCreatedUpdate update, MaxClient client) {

        if (!userService.isAdmin(update.getMessage().getSender().getUserId())) {
            return;
        }

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.ADD_CHALLENGE_INFO.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                        CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue()))))
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);

        // устанавливаем состояние add challenge
        stateRepository.put(update.getMessage().getSender().getUserId(), StateName.ADD_CHALLENGE);

        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
