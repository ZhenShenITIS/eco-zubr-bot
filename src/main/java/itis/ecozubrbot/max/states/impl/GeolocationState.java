package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.services.GeoLocationService;
import itis.ecozubrbot.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.LocationAttachment;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class GeolocationState implements State {
    private final StateName stateName = StateName.GEOLOCATION;

    private UserService userService;
    private GeoLocationService geoLocationService;
    private StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        String textToSend;
        boolean isChange = false;
        List<Attachment> attachments = update.getMessage().getBody().getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment a : attachments) {
                if ("location".equals(a.getType())) {
                    LocationAttachment location = (LocationAttachment) a;
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    String city = geoLocationService.getCity(lat, lon);
                    User user =
                            userService.getById(update.getMessage().getSender().getUserId());
                    user.setCity(city);
                    userService.save(user);
                    isChange = true;
                    stateRepository.put(user.getId(), StateName.DEFAULT);
                }
            }
        }

        textToSend = isChange
                ? StringConstants.GEOLOCATION_UPDATE_SUCCESS.getValue()
                : StringConstants.GEOLOCATION_UPDATE_FAIL.getValue();
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(textToSend).build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StateName getState() {
        return stateName;
    }
}
