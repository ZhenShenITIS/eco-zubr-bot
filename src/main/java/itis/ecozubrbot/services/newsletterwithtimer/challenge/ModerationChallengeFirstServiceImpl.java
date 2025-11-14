package itis.ecozubrbot.services.newsletterwithtimer.challenge;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.dto.ChatIdAndMessageBody;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.jpa.UserChallengeRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.newsletterwithtimer.ModerationChallengeFirstService;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.*;
import ru.max.botapi.queries.DeleteMessageQuery;

@Component
@AllArgsConstructor
public class ModerationChallengeFirstServiceImpl implements ModerationChallengeFirstService {

    UserRepository userRepository;
    ModerationChallengeServiceImpl moderationChallengeService;
    UserChallengeRepository userChallengeRepository;

    public void createModeration(UserChallenge userChallenge, MaxClient client) {

        List<UserChallenge> list = userChallengeRepository.findAll();
        list.sort(Comparator.comparing(UserChallenge::getId).reversed());
        for (UserChallenge userChallenge1 : list) {
            if (userChallenge1.getUser().getId().equals(userChallenge.getUser().getId())) {
                if (userChallenge1
                        .getChallenge()
                        .getId()
                        .equals(userChallenge.getChallenge().getId())) {
                    if (userChallenge1.getStatus() == TaskStatus.ACCEPTED) {
                        NewMessageBody messageBody = new NewMessageBody("ВЫ уже выполнили данный челендж", null, null);
                        new NewsletterManager(client)
                                .sendMessage(
                                        messageBody, userChallenge.getUser().getChatId());
                        return;
                    }
                }
            }
        }

        long idNewsLetter = userChallenge.getId();
        long chatIdSender = userChallenge.getUser().getChatId();

        // Здесь будет итератор
        List<ChatIdAndMessageBody> chatIdAndMessageBodies = new ArrayList<>();
        List<User> users = userRepository.findAll().stream()
                .filter(u -> !u.getChatId().equals(chatIdSender))
                .sorted(Comparator.comparing(User::getId).reversed())
                .toList();
        for (User user : users) {
            NewMessageBody messageBody;
            Button buttonAccept = new CallbackButton(
                    "newsletterT" + ":" + idNewsLetter + ":" + user.getChatId() + ":" + "A", "Одобрить");
            Button buttonReject = new CallbackButton(
                    "newsletterT" + ":" + idNewsLetter + ":" + user.getChatId() + ":" + "R", "Отклонить");

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
        moderationChallengeService.initializeChallengeModeration(
                idNewsLetter,
                chatIdSender,
                iterator,
                approvedMessage,
                rejectedMessage,
                client,
                userChallengeRepository,
                userRepository);
    }

    public void cameAnswer(MessageCallbackUpdate update, MaxClient client) {
        String payload = update.getCallback().getPayload();

        long idNewsLetter = Long.parseLong(payload.split(":")[1]);
        long chatIdModerator = Long.parseLong(payload.split(":")[2]);
        NewsLetterTimerAnswer answer =
                payload.split(":")[3].contains("A") ? NewsLetterTimerAnswer.APPROVED : NewsLetterTimerAnswer.REJECTED;

        moderationChallengeService.cameAnswer(idNewsLetter, chatIdModerator, answer);

        DeleteMessageQuery query =
                new DeleteMessageQuery(client, update.getMessage().getBody().getMid());
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
