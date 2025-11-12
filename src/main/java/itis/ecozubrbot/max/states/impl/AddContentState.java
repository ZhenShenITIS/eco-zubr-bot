package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.max.handlers.MessageCallbackHandler;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.services.ChallengeEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.*;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class AddContentState implements State {
    private final StateName stateName = StateName.ADD_CONTENT;
    private final ChallengeEventService challengeEventService;
    private StateRepository stateRepository;
    private MessageCallbackHandler messageCallbackHandler;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        // возвращаем default состояние и отправляем update по цепочке обработки хэндлерами
        stateRepository.put(update.getCallback().getUser().getUserId(), StateName.DEFAULT);
        messageCallbackHandler.handleMessageCallback(update, client);
    }

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {

        String text = update.getMessage().getBody().getText();

        // получаем прикреплённую картинку, если существует
        String photoToken = null;
        if (update.getMessage().getBody().getAttachments() != null) {
            for (Attachment attachment : update.getMessage().getBody().getAttachments()) {
                if ("image".equals(attachment.getType())) {
                    photoToken = ((PhotoAttachment) attachment).getPayload().getToken();
                    break;
                }
            }
        }

        NewMessageBody replyMessage;
        try {
            challengeEventService.add(text, photoToken);
            // возвращаем состояние на нормальное
            stateRepository.put(update.getMessage().getSender().getUserId(), StateName.DEFAULT);
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.ADD_CONTENT_SUCCESS.getValue())
                    .build();
        } catch (IncorrectJsonStringChallengeException e) {
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.ADD_CONTENT_FAIL.getValue())
                    .build();
        }

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
