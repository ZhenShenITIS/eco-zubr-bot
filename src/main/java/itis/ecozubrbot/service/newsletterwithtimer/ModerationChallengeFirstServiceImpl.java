package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import java.util.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.model.*;

@Component
@AllArgsConstructor
public class ModerationChallengeFirstServiceImpl implements ModerationChallengeFirstService{

    UserRepository userRepository;
    ModerationChallengeServiceImpl moderationChallengeService;

    public void createModeration(UserChallenge userChallenge) {
        long idNewsLetter = userChallenge.getId();
        long chatIdSender = userChallenge.getUser().getChatId();

        // Здесь будет итератор
        List<ChatIdAndMessageBody> chatIdAndMessageBodies = new ArrayList<>();
        List<User> users = userRepository.findAll().stream()
                .filter(u -> !u.getChatId().equals(chatIdSender))
                .toList();
        for (User user : users) {
            NewMessageBody messageBody;
            Button buttonAccept = new CallbackButton(idNewsLetter + ":" + user.getChatId() + ":" + "A", "Одобрить");
            Button buttonReject = new CallbackButton(idNewsLetter + ":" + user.getChatId() + ":" + "R", "Отклонить");

            List<Button> buttons = Arrays.asList(buttonAccept, buttonReject);
            messageBody = NewMessageBodyBuilder.ofText("Модерация на следующий челендж: "
                            + userChallenge.getChallenge().getTitle() + ": " + userChallenge.getProofDescription())
                    .withAttachments(AttachmentsBuilder.inlineKeyboard(
                                    InlineKeyboardBuilder.singleRow(buttonAccept, buttonReject))
                            .with(AttachmentsBuilder.photos(userChallenge.getProofImageUrl())))
                    .build();
            ChatIdAndMessageBody chatIdAndMessageBody = new ChatIdAndMessageBody(messageBody, user.getChatId());
            chatIdAndMessageBodies.add(chatIdAndMessageBody);
        }

        Iterator<ChatIdAndMessageBody> iterator = chatIdAndMessageBodies.iterator();

        // здесь сообщение isApproved
        NewMessageBody approvedMessage = new NewMessageBody(
                String.format(
                        "Модерация одобрила выполнение челенджа %s",
                        userChallenge.getChallenge().getTitle()),
                null,
                null);

        // здесь сообщение isRejected
        NewMessageBody rejectedMessage = new NewMessageBody(
                String.format(
                        "Модерация отклонила выполнение челенджа %s",
                        userChallenge.getChallenge().getTitle()),
                null,
                null);
        moderationChallengeService.initializeModeration(
                idNewsLetter, chatIdSender, iterator, approvedMessage, rejectedMessage);
    }

    public void cameAnswer(MessageCallbackUpdate update) {
        String payload = update.getCallback().getPayload();

        long idNewsLetter = Long.parseLong(payload.split(":")[0]);
        long chatIdModerator = Long.parseLong(payload.split(":")[1]);
        NewsLetterTimerAnswer answer =
                payload.split(":")[2].contains("A") ? NewsLetterTimerAnswer.APPROVED : NewsLetterTimerAnswer.REJECTED;

        moderationChallengeService.cameAnswer(idNewsLetter, chatIdModerator, answer);
    }
}
