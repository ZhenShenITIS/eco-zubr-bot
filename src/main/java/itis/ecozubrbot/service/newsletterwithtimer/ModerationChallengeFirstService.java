package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.models.UserChallenge;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

public interface ModerationChallengeFirstService {
    // Отправить на модерацию
    void createModeration(UserChallenge userChallenge, MaxClient client);

    // Получить ответ от модерации
    void cameAnswer(MessageCallbackUpdate update, MaxClient client);
}
