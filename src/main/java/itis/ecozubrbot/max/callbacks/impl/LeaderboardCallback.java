package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.services.LeaderboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class LeaderboardCallback implements Callback {
    private final CallbackName callbackName = CallbackName.LEADERBOARD;
    private final LeaderboardService leaderboardService;
    private BasicFileMap basicFileMap;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {

        String topForUser = leaderboardService.getLeaderboardForUser(
                update.getCallback().getUser().getUserId());

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(topForUser)
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                                CallbackName.BACK_TO_MENU.getCallbackName(),
                                StringConstants.BACK_TO_MENU_BUTTON.getValue())))
                        .with(AttachmentsBuilder.photos(basicFileMap.getToken(BasicFile.LEADERBOARD))))
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
