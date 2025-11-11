package itis.ecozubrbot.max.commands.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.CommandName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.commands.Command;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
public class MenuCommand implements Command {
    private final CommandName commandName = CommandName.MENU;

    @Override
    public CommandName getCommand() {
        return commandName;
    }

    @Override
    public void handleCommand(MessageCreatedUpdate update, MaxClient client) {
        List<List<Button>> buttonGrid = new ArrayList<>();
        List<Button> buttonRow1 = new ArrayList<>();
        buttonRow1.add(new CallbackButton(CallbackName.PET.getCallbackName(), StringConstants.PET_BUTTON.getValue()));
        buttonRow1.add(new CallbackButton(CallbackName.SHOP.getCallbackName(), StringConstants.SHOP_BUTTON.getValue()));
        buttonGrid.add(buttonRow1);
        List<Button> buttonRow2 = new ArrayList<>();
        buttonRow2.add(
                new CallbackButton(CallbackName.EVENTS.getCallbackName(), StringConstants.EVENTS_BUTTON.getValue()));
        buttonRow2.add(
                new CallbackButton(CallbackName.TASKS.getCallbackName(), StringConstants.TASKS_BUTTON.getValue()));
        buttonGrid.add(buttonRow2);
        List<Button> buttonRow3 = new ArrayList<>();
        buttonRow3.add(
                new CallbackButton(CallbackName.PROFILE.getCallbackName(), StringConstants.PROFILE_BUTTON.getValue()));
        buttonGrid.add(buttonRow3);
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.START.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(buttonGrid)))
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
