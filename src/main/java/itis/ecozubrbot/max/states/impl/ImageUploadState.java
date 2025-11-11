package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.services.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class ImageUploadState implements State {
    private StateRepository stateRepository;
    private final StateName stateName = StateName.UPLOAD_IMAGE;

    private UploadService uploadService;
    private BasicFileMap basicFileMap;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        String token = basicFileMap.getToken(BasicFile.PET_FIRST_STAGE);

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText("Картинка")
                .withAttachments(AttachmentsBuilder.photos(token))
                .build();
        Long chatId = update.getMessage().getRecipient().getChatId();
        SendMessageQuery query = new SendMessageQuery(client, replyMessage).chatId(chatId);
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        stateRepository.put(update.getMessage().getSender().getUserId(), StateName.DEFAULT);
    }

    @Override
    public StateName getState() {
        return stateName;
    }
}
