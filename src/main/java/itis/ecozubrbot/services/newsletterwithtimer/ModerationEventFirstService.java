package itis.ecozubrbot.services.newsletterwithtimer;

import itis.ecozubrbot.models.UserEvent;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

public interface ModerationEventFirstService {
    // Отправить на модерацию
    void createModeration(UserEvent userEvent, MaxClient client);

    // Получить ответ от модерации
    void cameAnswer(MessageCallbackUpdate update, MaxClient client);
}
