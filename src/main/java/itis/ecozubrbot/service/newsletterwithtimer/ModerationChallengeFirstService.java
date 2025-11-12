package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.models.UserChallenge;
import ru.max.botapi.model.MessageCallbackUpdate;

public interface ModerationChallengeFirstService {
    // Отправить на модерацию
    void createModeration(UserChallenge userChallenge);

    // Получить ответ от модерации
    void cameAnswer(MessageCallbackUpdate update);
}
