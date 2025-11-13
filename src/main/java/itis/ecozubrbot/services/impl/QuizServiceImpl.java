package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.QuizAnswer;
import itis.ecozubrbot.models.QuizQuestion;
import itis.ecozubrbot.repositories.jpa.QuizAnswerRepository;
import itis.ecozubrbot.repositories.jpa.QuizQuestionRepository;
import itis.ecozubrbot.services.QuizService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {
    private QuizQuestionRepository quizQuestionRepository;
    private QuizAnswerRepository quizAnswerRepository;

    @Override
    public void addQuiz(String jsonChallenge) throws IncorrectJsonStringChallengeException {
        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);
            QuizQuestion question = QuizQuestion.builder()
                    .text(jsonObject.getString("text"))
                    .textAfterAnswer(jsonObject.getString("text_after_answer"))
                    .experienceReward(jsonObject.getInt("experience_reward"))
                    .build();

            quizQuestionRepository.save(question);

            JSONArray answersJson = jsonObject.getJSONArray("answers");
            for (int i = 0; i < answersJson.length(); i++) {
                JSONObject q = answersJson.getJSONObject(i);
                QuizAnswer answer = QuizAnswer.builder()
                        .question(question)
                        .text(q.getString("text"))
                        .isCorrect(q.getBoolean("isCorrect"))
                        .build();
                quizAnswerRepository.save(answer);
            }

        } catch (Exception e) {
            throw new IncorrectJsonStringChallengeException();
        }
    }
}
