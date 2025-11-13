package itis.ecozubrbot.service.newsletterwithtimer.event;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.models.UserEvent;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.jpa.UserEventRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.service.newsletterwithtimer.ModerationEventFirstService;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.DeleteMessageQuery;

@Component
@AllArgsConstructor
public class ModerationEventFirstServiceImpl implements ModerationEventFirstService {

    UserRepository userRepository;
    ModerationEventServiceImpl moderationEventService;
    UserEventRepository userEventRepository;

    public void createModeration(UserEvent userEvent, MaxClient client) {

        List<UserEvent> list = userEventRepository.findAll();
        list.sort(Comparator.comparing(UserEvent::getId).reversed());
        for (UserEvent userChallenge1 : list) {
            if (userChallenge1.getUser().getId().equals(userEvent.getUser().getId())) {
                if (userChallenge1
                        .getEvent()
                        .getId()
                        .equals(userEvent.getEvent().getId())) {
                    if (userChallenge1.getStatus() == TaskStatus.ACCEPTED) {
                        NewMessageBody messageBody = new NewMessageBody("Вы уже выполнили данное событие", null, null);
                        new NewsletterManager(client)
                                .sendMessage(messageBody, userEvent.getUser().getChatId());
                        return;
                    }
                }
            }
        }

        long idNewsLetter = userEvent.getId();
        long chatIdSender = userEvent.getUser().getChatId();

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
            messageBody = NewMessageBodyBuilder.ofText("Модерация на следующее событие: "
                            + userEvent.getEvent().getTitle() + ": " + userEvent.getProofDescription())
                    .withAttachments(AttachmentsBuilder.inlineKeyboard(
                                    InlineKeyboardBuilder.singleRow(buttonAccept, buttonReject))
                            .with(AttachmentsBuilder.photos(userEvent.getProofImageUrl())))
                    .build();
            ChatIdAndMessageBody chatIdAndMessageBody = new ChatIdAndMessageBody(messageBody, user.getChatId());
            chatIdAndMessageBodies.add(chatIdAndMessageBody);
        }

        Iterator<ChatIdAndMessageBody> iterator = chatIdAndMessageBodies.iterator();

        // здесь сообщение isApproved
        NewMessageBody approvedMessage = new NewMessageBody(
                String.format(
                        "Модерация одобрила выполнение события %s",
                        userEvent.getEvent().getTitle()),
                null,
                null);

        // здесь сообщение isRejected
        NewMessageBody rejectedMessage = new NewMessageBody(
                String.format(
                        "Модерация отклонила выполнение события %s",
                        userEvent.getEvent().getTitle()),
                null,
                null);
        moderationEventService.initializeEventModeration(
                idNewsLetter,
                chatIdSender,
                iterator,
                approvedMessage,
                rejectedMessage,
                client,
                userEventRepository,
                userRepository);
    }

    public void cameAnswer(MessageCallbackUpdate update, MaxClient client) {
        String payload = update.getCallback().getPayload();

        long idNewsLetter = Long.parseLong(payload.split(":")[1]);
        long chatIdModerator = Long.parseLong(payload.split(":")[2]);
        NewsLetterTimerAnswer answer =
                payload.split(":")[3].contains("A") ? NewsLetterTimerAnswer.APPROVED : NewsLetterTimerAnswer.REJECTED;

        moderationEventService.cameAnswer(idNewsLetter, chatIdModerator, answer);

        DeleteMessageQuery query =
                new DeleteMessageQuery(client, update.getMessage().getBody().getMid());
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
