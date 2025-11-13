package itis.ecozubrbot.max.states.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.models.UserEvent;
import itis.ecozubrbot.repositories.StateRepository;
import itis.ecozubrbot.repositories.UserEventOnModerationRepository;
import itis.ecozubrbot.services.UserEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.PhotoAttachment;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class WaitingProofOfEventState implements State {
    private final StateName stateName = StateName.WAITING_PROOF_OF_EVENT;
    private final UserEventService userEventService;
    private final UserEventOnModerationRepository userEventOnModerationRepository;
    private final StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
    }

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        Long chatId = update.getMessage().getRecipient().getChatId();
        Long userId = update.getMessage().getSender().getUserId();
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

        if (text == null || photoToken == null) {
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.SEND_PHOTO_AND_TEXT.getValue())
                    .build();
        } else {
            replyMessage = NewMessageBodyBuilder.ofText(
                            StringConstants.PROOF_CHECK.getValue().formatted(text))
                    .withAttachments(AttachmentsBuilder.photos(photoToken)
                            .with(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.singleRow(new CallbackButton(
                                    CallbackName.EVENT_ACCEPT_FOR_SENDING_PROOF.getCallbackName(),
                                    StringConstants.ACCEPT_PROOF_FOR_SENDING.getValue())))))
                    .build();
            UserEvent userEvent =
                    userEventService.getById(userEventOnModerationRepository.getUserEventId(userId));
            userEvent.setProofDescription(text);
            userEvent.setProofImageUrl(photoToken);
            userEventService.save(userEvent);
            stateRepository.put(userId, StateName.DEFAULT);
        }

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
