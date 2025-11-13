package itis.ecozubrbot.quiz;

import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;

@Component
public class QuizThread extends Thread {

    private QuizService quizService;
    private QuizGetAnswerService quizGetAnswerService;
    private MaxClient client;

    public QuizThread(QuizService quizService, QuizGetAnswerService quizGetAnswerService) {
        this.quizService = quizService;
        this.quizGetAnswerService = quizGetAnswerService;
    }

    public void setClient(MaxClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            sleep(10000);
            while (true) {
                quizService.initializeQuizNewsLetter(client);
                sleep(1000 * 60 * 60 * 24);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
