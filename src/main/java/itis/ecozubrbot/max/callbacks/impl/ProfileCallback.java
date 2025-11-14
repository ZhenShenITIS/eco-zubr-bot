package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.services.ProfileService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class ProfileCallback implements Callback {
    private final CallbackName callbackName = CallbackName.PROFILE;
    private final ProfileService profileService;
    private BasicFileMap basicFileMap;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {

        String userProfileString =
                profileService.getProfileForUser(update.getCallback().getUser().getUserId());

        List<List<Button>> buttonGrid = new ArrayList<>();

        List<Button> buttonRow1 = new ArrayList<>();
        buttonRow1.add(new CallbackButton(
                CallbackName.CHANGE_CITY.getCallbackName(), StringConstants.CHANGE_CITY_BUTTON.getValue()));

        List<Button> buttonRow2 = new ArrayList<>();
        buttonRow2.add(new CallbackButton(
                CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue()));

        buttonGrid.add(buttonRow1);
        buttonGrid.add(buttonRow2);

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(userProfileString)
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(buttonGrid))
                        .with(AttachmentsBuilder.photos(basicFileMap.getToken(BasicFile.PROFILE))))
                .build();

        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
