package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import ru.max.botapi.model.NewMessageBody;

import java.util.Iterator;

public interface ModerationService {
    // Инициализировать поток модерации.
    void initializeModeration(long idNewsLetter, long ChatIdSender, Iterator<ChatIdAndMessageBody> iterator, NewMessageBody isApproved,  NewMessageBody isRejected);

    // Прислать ответ от модерации, которая проверила юзера с ChatIdSender.
    void cameAnswer(long idNewsLetter, long chatId, NewsLetterTimerAnswer answer);

    // Отправить результаты модерации в этот класс для дальнейшей обработка
    void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected);

}
