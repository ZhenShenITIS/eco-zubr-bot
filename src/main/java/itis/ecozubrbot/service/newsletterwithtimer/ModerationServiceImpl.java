package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.MessageTimerRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.User;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

record NewsLetterContext(long ChatIdSender, ModerationSendingManager manager, NewMessageBody isApproved, NewMessageBody isRejected) {}

public class ModerationServiceImpl implements ModerationService {

    NewsletterManager newsletterManager;
    MessageTimerRepository messageTimerRepository;
    UserRepository userRepository;
    //TODO: Извне отсылают челендж или событие, а я генерирую айди рассылки по нему

    private final int COUNT_MODERATORS = 5;
    private final int COUNT_CHAT_SEC = 60 * 60;
    private final int COUNT_MAX_TIME_WORK_SEC = 24 * 60 * 60;


    private final ConcurrentHashMap<Long, NewsLetterContext> idNewsletterToContext = new  ConcurrentHashMap<>();

    @Override
    public void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected) {
        NewsLetterContext context = idNewsletterToContext.get(idNewsletter);

        //Отправить ответ пользователю
        if(checkIsApproved(countApproved, countRejected)) {
            newsletterManager.sendMessage(context.isApproved(), context.ChatIdSender());
        }
        else {
            newsletterManager.sendMessage(context.isRejected(), context.ChatIdSender());
        }

        //Обновить бд

    }

    private boolean checkIsApproved(long countApproved, long countRejected) {
        return countApproved >= countRejected;
    }

    @Override
    public void initializeModeration(long idNewsLetter, long chatIdSender, Iterator<ChatIdAndMessageBody> iterator, NewMessageBody isApproved, NewMessageBody isRejected) {
        // Создать менеджер управление рассылкой
        ModerationSendingManager manager = new ModerationSendingManager(
                newsletterManager,
                messageTimerRepository,
                this
        );

        //Сохранить контекст рассылки
        idNewsletterToContext.put(idNewsLetter, new NewsLetterContext(
                chatIdSender, manager, isApproved, isRejected
        ));

        //Создать поток для менеджера
        Thread.startVirtualThread(() -> manager.activateNewsletterModeration(iterator, idNewsLetter,
                COUNT_MODERATORS, COUNT_CHAT_SEC, COUNT_MAX_TIME_WORK_SEC));
    }

    @Override
    public void cameAnswer(long idNewsLetter, long chatId, NewsLetterTimerAnswer answer) {
        ModerationSendingManager manager = idNewsletterToContext.get(idNewsLetter).manager();
        manager.cameAnswer(idNewsLetter, chatId, answer);
    }
}

