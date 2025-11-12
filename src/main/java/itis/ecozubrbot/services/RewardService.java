package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;

public interface RewardService {
    void addReward(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException;
}
