package itis.ecozubrbot.max.handlers;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.UserMapper;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.RequestGeoLocationButton;
import ru.max.botapi.model.Update;
import ru.max.botapi.queries.SendMessageQuery;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class BotStartedHandler {

    private UserService userService;

    private StateRepository stateRepository;

    public void onBotStarted (BotStartedUpdate update, MaxClient client) {
        Long chatId = update.getChatId();
        User user = UserMapper.getEntityFromMaxUser(update.getUser(), chatId);
        user.setCreatedDate(LocalDate.now());
        userService.save(user);
        NewMessageBody replyMessage = NewMessageBodyBuilder
                .ofText(StringConstants.START_BOT_UPDATE.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new RequestGeoLocationButton(StringConstants.GEOLOCATION_BUTTON.getValue()))))
                .build();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
            stateRepository.put(user.getId(), StateName.GEOLOCATION);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

    }
}
