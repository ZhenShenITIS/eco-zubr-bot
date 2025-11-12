package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.max.botapi.model.NewMessageBody;

public class ModerationChallengeFirstService {

    UserRepository userRepository;

    public void createModeration(UserChallenge userChallenge) {
        long idNewsLetter = userChallenge.getId();
        long chatIdSender = Objects.requireNonNull(
                        userRepository.findById(idNewsLetter).orElse(null))
                .getChatId();

        // Здесь будет итератор
        List<ChatIdAndMessageBody> chatIdAndMessageBodies = new ArrayList<>();

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
    }
}
