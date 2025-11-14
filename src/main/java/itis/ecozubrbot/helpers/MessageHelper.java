package itis.ecozubrbot.helpers;

import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.model.NewMessageBody;

public class MessageHelper {
    public static NewMessageBody getNewMessageBody(
            String text, InlineKeyboardBuilder inlineKeyboardBuilder, String photoToken) {
        if (inlineKeyboardBuilder != null && photoToken != null) {
            return newMessageTextWithPhotoAndInlineKeyboard(text, inlineKeyboardBuilder, photoToken);
        } else if (inlineKeyboardBuilder != null) {
            return getMessageTextWithInlineKeyboard(text, inlineKeyboardBuilder);
        } else if (photoToken != null) {
            return getMessageTextWithPhoto(text, photoToken);
        } else {
            return getMessageOnlyText(text);
        }
    }

    private static NewMessageBody getMessageOnlyText(String text) {
        return NewMessageBodyBuilder.ofText(text).build();
    }

    private static NewMessageBody getMessageTextWithPhoto(String text, String token) {
        return NewMessageBodyBuilder.ofText(text)
                .withAttachments(AttachmentsBuilder.photos(token))
                .build();
    }

    private static NewMessageBody getMessageTextWithInlineKeyboard(
            String text, InlineKeyboardBuilder inlineKeyboardBuilder) {
        return NewMessageBodyBuilder.ofText(text)
                .withAttachments(AttachmentsBuilder.inlineKeyboard(inlineKeyboardBuilder))
                .build();
    }

    private static NewMessageBody newMessageTextWithPhotoAndInlineKeyboard(
            String text, InlineKeyboardBuilder inlineKeyboardBuilder, String photoToken) {
        return NewMessageBodyBuilder.ofText(text)
                .withAttachments(AttachmentsBuilder.inlineKeyboard(inlineKeyboardBuilder)
                        .with(AttachmentsBuilder.photos(photoToken)))
                .build();
    }
}
