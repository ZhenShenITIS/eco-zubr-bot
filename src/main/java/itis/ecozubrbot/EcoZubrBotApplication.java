package itis.ecozubrbot;

import itis.ecozubrbot.admincontrol.AdminControl;
import itis.ecozubrbot.fillsql.FillSqlThread;
import itis.ecozubrbot.max.EcoZubrBot;
import itis.ecozubrbot.quiz.QuizThread;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
@AllArgsConstructor
public class EcoZubrBotApplication {

    QuizThread quizThread;
    FillSqlThread  fillSqlThread;

    public static void main(String[] args) {
        SpringApplication.run(EcoZubrBotApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EcoZubrBot bot) {
        return args -> {
            bot.start();
            new AdminControl(bot).start();
            quizThread.setClient(bot.getClient());
            quizThread.start();
            fillSqlThread.start();
        };
    }
}
