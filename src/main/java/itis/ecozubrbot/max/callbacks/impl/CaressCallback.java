package itis.ecozubrbot.max.callbacks.impl;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.helpers.PetUtils;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Pet;
import itis.ecozubrbot.repositories.jpa.PetRepository;
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
public class CaressCallback implements Callback {
    private final CallbackName callbackName = CallbackName.CARESS;

    private PetRepository petRepository;

    private PetUtils petUtils;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long chatId = update.getMessage().getRecipient().getChatId();
        Pet pet = petRepository
                .findById(update.getCallback().getUser().getUserId())
                .orElse(null);
        String token = petUtils.getImageOfPetWithHearts(pet.getExperience());
        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.CARESS_ANSWER.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                                CallbackName.BACK_TO_PET_START.getCallbackName(),
                                StringConstants.BACK_BUTTON.getValue())))
                        .with(AttachmentsBuilder.photos(token)))
                .build();
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
