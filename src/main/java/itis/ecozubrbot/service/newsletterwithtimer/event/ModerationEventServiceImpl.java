package itis.ecozubrbot.service.newsletterwithtimer.event;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.models.Pet;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.models.UserEvent;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.MessageTimerRepository;
import itis.ecozubrbot.repositories.impl.MapMessageTimerRepository;
import itis.ecozubrbot.repositories.jpa.*;
import itis.ecozubrbot.service.newsletterwithtimer.ModerationService;
import itis.ecozubrbot.service.newsletterwithtimer.challenge.ModerationSendingManager;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.NewMessageBody;

@Component
public class ModerationEventServiceImpl implements ModerationService {

    private final EventRepository eventRepository;

    public ModerationEventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    record NewsLetterContext(
            long ChatIdSender,
            ModerationSendingManager manager,
            NewMessageBody isApproved,
            NewMessageBody isRejected) {}

    UserRepository userRepository;
    NewsletterManager newsletterManager;
    MessageTimerRepository messageTimerRepository;
    UserEventRepository userEventRepository;
    PetRepository petRepository;

    // TODO: ИЗМЕНИТ НА 2
    private final int COUNT_MODERATORS = 3;
    private final int COUNT_CHAT_SEC = 60 * 60;
    private final int COUNT_MAX_TIME_WORK_SEC = 24 * 60 * 60;

    private final ConcurrentHashMap<Long, NewsLetterContext> idNewsletterToContext = new ConcurrentHashMap<>();

    public void setResultNewsletterTimer(long idNewsletter, long countApproved, long countRejected) {
        NewsLetterContext context = idNewsletterToContext.get(idNewsletter);

        UserEvent userEvent = userEventRepository.findById(idNewsletter).orElse(null);

        // Отправить ответ пользователю
        if (checkIsApproved(countApproved, countRejected)) {
            userEvent.setStatus(TaskStatus.ACCEPTED);
            newsletterManager.sendMessage(context.isApproved(), context.ChatIdSender());
        } else {
            userEvent.setStatus(TaskStatus.REJECTED);
            newsletterManager.sendMessage(context.isRejected(), context.ChatIdSender());
        }

        // Обновить бд
        userEventRepository.save(userEvent);
        User user = userEvent.getUser();
        Integer userPoints = user.getPoints();
        Pet pet = user.getPet();
        if (userPoints == null) {
            userPoints = 0;
        }
        pet.setExperience(pet.getExperience() + userEvent.getEvent().getExperienceReward());
        user.setPoints(user.getPoints() + userEvent.getEvent().getPointsReward());

        petRepository.save(pet);
        userRepository.save(user);
    }

    private boolean checkIsApproved(long countApproved, long countRejected) {
        return countApproved >= countRejected;
    }

    @Override
    public void initializeChallengeModeration(
            long idNewsLetter,
            long ChatIdSender,
            Iterator<ChatIdAndMessageBody> iterator,
            NewMessageBody isApproved,
            NewMessageBody isRejected,
            MaxClient client,
            UserChallengeRepository userChallengeRepository,
            UserRepository userRepository) {
        throw new RuntimeException("Not implemented");
    }

    public void initializeEventModeration(
            long idNewsLetter,
            long chatIdSender,
            Iterator<ChatIdAndMessageBody> iterator,
            NewMessageBody isApproved,
            NewMessageBody isRejected,
            MaxClient client,
            UserEventRepository userEventRepository,
            PetRepository petRepository,
            UserRepository userRepository) {

        this.userRepository = userRepository;
        this.petRepository = petRepository;
        newsletterManager = new NewsletterManager(client);
        messageTimerRepository = new MapMessageTimerRepository();
        this.userEventRepository = userEventRepository;
        // Создать менеджер управление рассылкой
        ModerationSendingManager manager =
                new ModerationSendingManager(newsletterManager, messageTimerRepository, this);

        // Сохранить контекст рассылки
        idNewsletterToContext.put(idNewsLetter, new NewsLetterContext(chatIdSender, manager, isApproved, isRejected));

        // Создать поток для менеджера
        Thread.startVirtualThread(() -> manager.activateNewsletterModeration(
                iterator, idNewsLetter, COUNT_MODERATORS, COUNT_CHAT_SEC, COUNT_MAX_TIME_WORK_SEC));
    }

    public void cameAnswer(long idNewsLetter, long chatId, NewsLetterTimerAnswer answer) {
        ModerationSendingManager manager =
                idNewsletterToContext.get(idNewsLetter).manager();
        manager.cameAnswer(idNewsLetter, chatId, answer);
    }
}
