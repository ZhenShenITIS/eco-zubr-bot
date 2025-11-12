package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;

public interface ContentService {
    void add(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException;
}
