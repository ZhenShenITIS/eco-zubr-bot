package itis.ecozubrbot.quiz;

import itis.ecozubrbot.models.QuizAnswer;
import itis.ecozubrbot.models.QuizQuestion;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.models.UserQuizResponse;
import itis.ecozubrbot.newsletter.NewsletterManager;
import itis.ecozubrbot.repositories.jpa.QuizAnswerRepository;
import itis.ecozubrbot.repositories.jpa.QuizQuestionRepository;
import itis.ecozubrbot.repositories.jpa.UserQuizResponseRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.NewMessageBody;

@Component
@AllArgsConstructor
public class QuizService {

    UserRepository userRepository;
    QuizAnswerRepository quizAnswerRepository;
    QuizQuestionRepository quizQuestionRepository;
    UserQuizResponseRepository userQuizResponseRepository;

    public void initializeQuizNewsLetter(MaxClient client) {
        List<User> users = userRepository.findAll();
        List<QuizQuestion> quizQuestions = quizQuestionRepository.findAll();
        for (User user : users) {
            QuizQuestion quizQuestion = null;
            List<UserQuizResponse> userQuizResponses = userQuizResponseRepository.findAll().stream()
                    .filter(userQuizResponse ->
                            Objects.equals(userQuizResponse.getUser().getId(), user.getId()))
                    .toList();
            for (QuizQuestion q : quizQuestions) {
                UserQuizResponse q1 = userQuizResponses.stream()
                        .filter(userQuizResponse ->
                                userQuizResponse.getQuestion().getId().equals(q.getId()))
                        .findFirst()
                        .orElse(null);
                if (q1 == null) {
                    quizQuestion = q;
                }
            }

            if (quizQuestion != null) {
                addNewsLetteerQuis(client, quizQuestion, user);
            }
        }
    }

    private void addNewsLetteerQuis(MaxClient client, QuizQuestion quizQuestion, User user) {
        List<QuizAnswer> quizAnswers = quizQuestion.getAnswers();

        List<Button> buttons = new ArrayList<>();

        List<String> options = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) options.add(String.valueOf((char) i));
        int i = 0;

        StringBuilder text = new StringBuilder("Квиз: " + quizQuestion.getText() + "\n");

        for (QuizAnswer q : quizAnswers) {
            buttons.add(
                    new CallbackButton("newsletterQ" + ":" + quizQuestion.getId() + ":" + q.getId(), options.get(i)));
            text.append(options.get(i++)).append(") ").append(q.getText()).append("\n");
        }
        List<List<Button>> buttonsList = new ArrayList<>();
        for (Button b : buttons) {
            if (buttonsList.isEmpty() || buttonsList.getLast().size() >= 2) {
                buttonsList.add(new ArrayList<>());
            }
            buttonsList.getLast().add(b);
        }

        // Создаем сообщение
        NewMessageBody messageBody = NewMessageBodyBuilder.ofText(text.toString())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(buttonsList)))
                .build();
        NewsletterManager newsletterManager = new NewsletterManager(client);
        newsletterManager.sendMessage(messageBody, user.getChatId());
    }
}
