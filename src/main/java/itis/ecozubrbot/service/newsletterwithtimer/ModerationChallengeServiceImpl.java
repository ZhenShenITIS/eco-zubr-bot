package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.MessageTimerRepository;
import itis.ecozubrbot.repositories.jpa.UserChallengeRepository;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import ru.max.botapi.model.NewMessageBody;

record NewsLetterContext(
        long ChatIdSender, ModerationSendingManager manager, NewMessageBody isApproved, NewMessageBody isRejected) {}

@Component
public class ModerationChallengeServiceImpl implements ModerationService {

    NewsletterManager newsletterManager;
    MessageTimerRepository messageTimerRepository;
    UserChallengeRepository userChallengeRepository;

    private final int COUNT_MODERATORS = 5;
    private final int COUNT_CHAT_SEC = 60 * 60;
    private final int COUNT_MAX_TIME_WORK_SEC = 24 * 60 * 60;

    private final ConcurrentHashMap<Long, NewsLetterContext> idNewsletterToContext = new ConcurrentHashMap<>();

    @Override
    public void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected) {
        NewsLetterContext context = idNewsletterToContext.get(idNewsletter);

        UserChallenge userChallenge =
                userChallengeRepository.findById(idNewsletter).orElse(null);

        // Отправить ответ пользователю
        if (checkIsApproved(countApproved, countRejected)) {
            userChallenge.setStatus(TaskStatus.ACCEPTED);
            newsletterManager.sendMessage(context.isApproved(), context.ChatIdSender());
        } else {
            userChallenge.setStatus(TaskStatus.REJECTED);
            newsletterManager.sendMessage(context.isRejected(), context.ChatIdSender());
        }

        // Обновить бд
        userChallengeRepository.save(userChallenge);
    }

    private boolean checkIsApproved(long countApproved, long countRejected) {
        return countApproved >= countRejected;
    }

    @Override
    public void initializeModeration(
            long idNewsLetter,
            long chatIdSender,
            Iterator<ChatIdAndMessageBody> iterator,
            NewMessageBody isApproved,
            NewMessageBody isRejected) {
        // Создать менеджер управление рассылкой
        ModerationSendingManager manager =
                new ModerationSendingManager(newsletterManager, messageTimerRepository, this);

        // Сохранить контекст рассылки
        idNewsletterToContext.put(idNewsLetter, new NewsLetterContext(chatIdSender, manager, isApproved, isRejected));

        // Создать поток для менеджера
        Thread.startVirtualThread(() -> manager.activateNewsletterModeration(
                iterator, idNewsLetter, COUNT_MODERATORS, COUNT_CHAT_SEC, COUNT_MAX_TIME_WORK_SEC));
    }

    @Override
    public void cameAnswer(long idNewsLetter, long chatId, NewsLetterTimerAnswer answer) {
        ModerationSendingManager manager =
                idNewsletterToContext.get(idNewsLetter).manager();
        manager.cameAnswer(idNewsLetter, chatId, answer);
    }
}
