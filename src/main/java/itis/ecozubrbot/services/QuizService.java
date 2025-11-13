package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;

public interface QuizService {
    void addQuiz(String jsonChallenge) throws IncorrectJsonStringChallengeException;
}
