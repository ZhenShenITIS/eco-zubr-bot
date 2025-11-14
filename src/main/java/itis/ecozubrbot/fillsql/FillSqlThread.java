package itis.ecozubrbot.fillsql;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.models.QuizQuestion;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.ChallengeRepository;
import itis.ecozubrbot.repositories.jpa.EventRepository;
import itis.ecozubrbot.repositories.jpa.QuizQuestionRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FillSqlThread extends Thread{

    FillSqlService fillSqlService;
    UserRepository  userRepository;
    ChallengeRepository  challengeRepository;
    EventRepository eventRepository;
    QuizQuestionRepository quizQuestionRepository;


    @Override
    public void run(){
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            return;
        }
        List<Challenge> challenges = challengeRepository.findAll();
        if(!challenges.isEmpty()){
            return;
        }
        List<Event> events = eventRepository.findAll();
        if(!events.isEmpty()){
            return;
        }
        List<QuizQuestion> quizQuestions = quizQuestionRepository.findAll();
        if(!quizQuestions.isEmpty()){
            return;
        }
        try {
            fillSqlService.fillSql();
        } catch (IncorrectJsonStringChallengeException e) {
            throw new RuntimeException(e);
        }
    }
}
