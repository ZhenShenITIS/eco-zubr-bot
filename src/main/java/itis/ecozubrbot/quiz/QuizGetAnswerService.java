package itis.ecozubrbot.quiz;

import itis.ecozubrbot.models.*;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.jpa.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.DeleteMessageQuery;

@Component
@AllArgsConstructor
public class QuizGetAnswerService {

    UserRepository userRepository;
    QuizQuestionRepository quizQuestionRepository;
    QuizAnswerRepository quizAnswerRepository;
    UserQuizResponseRepository userQuizResponseRepository;
    PetRepository petRepository;

    public void cameAnswer(MessageCallbackUpdate update, MaxClient client) {
        Long QuizQuestionId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        Long QuizAnswerId = Long.parseLong(update.getCallback().getPayload().split(":")[2]);
        User user = userRepository
                .findById(update.getCallback().getUser().getUserId())
                .orElse(null);

        QuizQuestion quizQuestion =
                quizQuestionRepository.findById(QuizQuestionId).orElse(null);
        QuizAnswer quizAnswerUser = quizAnswerRepository.findById(QuizAnswerId).orElse(null);

        DeleteMessageQuery query =
                new DeleteMessageQuery(client, update.getMessage().getBody().getMid());
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        UserQuizResponse userQuizResponse = UserQuizResponse.builder()
                .user(user)
                .answer(quizAnswerUser)
                .question(quizQuestion)
                .build();
        userQuizResponseRepository.save(userQuizResponse);

        assert quizAnswerUser != null;
        if (quizAnswerUser.isCorrect()) {
            Pet pet = user.getPet();
            pet.setExperience(pet.getExperience() + quizQuestion.getExperienceReward());
            petRepository.save(pet);
            userRepository.save(user);

            sendMessage(quizQuestion, user.getChatId(), true, client);
        } else {
            sendMessage(quizQuestion, user.getChatId(), false, client);
        }
    }

    private void sendMessage(QuizQuestion question, Long chatId, boolean isCorr, MaxClient client) {
        String predict = isCorr ? "Верный ответ: " : "Не верный ответ: ";
        NewMessageBody newMessageBody = new NewMessageBody(predict + question.getTextAfterAnswer(), null, null);
        NewsletterManager newsletterManager = new NewsletterManager(client);
        newsletterManager.sendMessage(newMessageBody, chatId);
    }
}
