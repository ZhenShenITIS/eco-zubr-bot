package itis.ecozubrbot.services.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.dto.ChatIdAndMessageBody;
import itis.ecozubrbot.repositories.jpa.PetRepository;
import itis.ecozubrbot.repositories.jpa.UserChallengeRepository;
import itis.ecozubrbot.repositories.jpa.UserEventRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import java.util.Iterator;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.NewMessageBody;

public interface ModerationService {
    // Инициализировать поток модерации челенджа.
    void initializeChallengeModeration(
            long idNewsLetter,
            long ChatIdSender,
            Iterator<ChatIdAndMessageBody> iterator,
            NewMessageBody isApproved,
            NewMessageBody isRejected,
            MaxClient client,
            UserChallengeRepository userChallengeRepository,
            UserRepository userRepository);

    // Инициализировать поток модерации События
    void initializeEventModeration(
            long idNewsLetter,
            long ChatIdSender,
            Iterator<ChatIdAndMessageBody> iterator,
            NewMessageBody isApproved,
            NewMessageBody isRejected,
            MaxClient client,
            UserEventRepository userEventRepository,
            PetRepository petRepository,
            UserRepository userRepository);

    // Прислать ответ от модерации, которая проверила юзера с ChatIdSender.
    void cameAnswer(long idNewsLetter, long chatId, NewsLetterTimerAnswer answer);

    // Отправить результаты модерации в этот класс для дальнейшей обработка
    void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected);
}

// idNewsLetter - это id UserChallenge
