package itis.ecozubrbot.service.newsletterwithtimer;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.NewsletterTimerStatus;
import itis.ecozubrbot.model.ChatIdAndMessageBody;
import itis.ecozubrbot.model.MessageTimer;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.MessageTimerRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import ru.max.botapi.model.NewMessageBody;

public class ModerationSendingManager {
    NewsletterManager newsletterManager;
    MessageTimerRepository messageTimerRepository;
    ModerationService moderationService;

    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> mainTaskFuture;
    private ScheduledFuture<?> shutdownFuture;

    public ModerationSendingManager(
            NewsletterManager newsletterManager,
            MessageTimerRepository messageTimerRepository,
            ModerationService moderationService) {
        this.newsletterManager = newsletterManager;
        this.messageTimerRepository = messageTimerRepository;
        this.moderationService = moderationService;
        scheduler = Executors.newScheduledThreadPool(2);
    }

    public void activateNewsletterModeration(
            Iterator<ChatIdAndMessageBody> moderatorsChat,
            long idNewsletter,
            int countModerators,
            long countChatSec,
            long maxTimeWorkSec) {
        // Первоначальная рассылка для модерации первым countMiderators пользователям
        while (moderatorsChat.hasNext() && countModerators-- > 0) {
            ChatIdAndMessageBody chatIdAndMessageBody = moderatorsChat.next();
            Long chatId = chatIdAndMessageBody.chat_id();
            NewMessageBody newMessageBody = chatIdAndMessageBody.newMessageBody();
            newsletterManager.sendMessage(newMessageBody, chatId);
            messageTimerRepository.save(
                    new MessageTimer(idNewsletter, chatId, NewsletterTimerStatus.WAITING, LocalDateTime.now()));
        }

        int finalCountModerators = countModerators;
        mainTaskFuture = scheduler.scheduleAtFixedRate(
                () -> tick(moderatorsChat, idNewsletter, finalCountModerators, countChatSec), 0, 2, TimeUnit.MINUTES);

        shutdownFuture = scheduler.schedule(
                () -> stop(moderatorsChat, idNewsletter, finalCountModerators, countChatSec),
                maxTimeWorkSec,
                TimeUnit.SECONDS);
    }

    // Проверка состояния рассылки
    private void tick(
            Iterator<ChatIdAndMessageBody> moderatorsChat, long idNewsletter, int countModerators, long countChatSec) {
        // Считаем количество просрочивших время
        int countNewInactive = changeStatusInactive(idNewsletter, countModerators);

        // Если набрано нужное количетво голосов или больш некого опрашивать
        if (checkIsComplete(idNewsletter, countNewInactive) || !moderatorsChat.hasNext()) {
            stop(moderatorsChat, idNewsletter, countNewInactive, countChatSec);
            return;
        }

        // Отправляем рассылку countNewInactive людям
        while (moderatorsChat.hasNext() && countNewInactive-- > 0) {
            ChatIdAndMessageBody chatIdAndMessageBody = moderatorsChat.next();
            Long chatId = chatIdAndMessageBody.chat_id();
            NewMessageBody newMessageBody = chatIdAndMessageBody.newMessageBody();
            newsletterManager.sendMessage(newMessageBody, chatId);
            messageTimerRepository.save(
                    new MessageTimer(idNewsletter, chatId, NewsletterTimerStatus.WAITING, LocalDateTime.now()));
        }
    }

    // Завершение рассылки с таймером
    private void stop(
            Iterator<ChatIdAndMessageBody> moderatorsChat, long idNewsletter, int countModerators, long countChatSec) {
        // Закрываем задачи
        if (mainTaskFuture != null) {
            mainTaskFuture.cancel(false);
        }
        if (shutdownFuture != null) {
            shutdownFuture.cancel(false);
        }

        scheduler.shutdown();

        // Отправяем результаты опроса в сервис
        List<MessageTimer> messageTimerList = messageTimerRepository.findByNewsLetterId(idNewsletter);
        moderationService.setResultNewsletterTimer(
                idNewsletter,
                messageTimerList.stream()
                        .filter((a) -> a.getStatus() == NewsletterTimerStatus.REPLIED
                                && a.getAnswer() == NewsLetterTimerAnswer.APPROVED)
                        .count(),
                messageTimerList.stream()
                        .filter((a) -> a.getStatus() == NewsletterTimerStatus.REPLIED
                                && a.getAnswer() == NewsLetterTimerAnswer.REJECTED)
                        .count());

        // Закрываем екзекутор
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Проверка, достаточно ли ответов
    private boolean checkIsComplete(long idNewsletter, int countModerators) {
        return messageTimerRepository.findByNewsLetterId(idNewsletter).stream()
                        .filter((a) -> a.getStatus() == NewsletterTimerStatus.REPLIED)
                        .count()
                >= countModerators;
    }

    // Обновление статусов у пользователей. Возвращает количество обновленных записей на timeout
    private int changeStatusInactive(long idNewsletter, long countChatSec) {
        List<MessageTimer> messageTimers = messageTimerRepository.findByNewsLetterId(idNewsletter);
        int count = 0;
        for (MessageTimer messageTimer : messageTimers) {
            if (messageTimer.getStatus() == NewsletterTimerStatus.WAITING) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime timeSend = messageTimer.getTimeSend();
                long sec = Math.abs(now.until(timeSend, ChronoUnit.SECONDS));
                if (sec > countChatSec) {
                    messageTimer.setStatus(NewsletterTimerStatus.TIMEOUT);
                    messageTimerRepository.update(messageTimer);
                    count++;
                }
            }
            if (messageTimer.getStatus() == NewsletterTimerStatus.REPLIED) {
                LocalDateTime timeResponse = messageTimer.getTimeResponse();
                LocalDateTime timeSend = messageTimer.getTimeSend();
                long sec = Math.abs(timeResponse.until(timeSend, ChronoUnit.SECONDS));
                if (sec > countChatSec) {
                    messageTimer.setStatus(NewsletterTimerStatus.TIMEOUT);
                    messageTimerRepository.update(messageTimer);
                    count++;
                }
            }
        }
        return count;
    }

    // Приешл ответ от юзера, обновим репозиторий
    public void cameAnswer(long idNewsletter, long chatId, NewsLetterTimerAnswer answer) {
        messageTimerRepository.findByNewsLetterId(idNewsletter).stream()
                .filter((a) -> a.getChatId() == chatId)
                .findFirst()
                .ifPresent(messageTimer -> {
                    messageTimer.setStatus(NewsletterTimerStatus.REPLIED);
                    messageTimer.setTimeResponse(LocalDateTime.now());
                    messageTimer.setAnswer(answer);
                    messageTimerRepository.update(messageTimer);
                });
    }
}
