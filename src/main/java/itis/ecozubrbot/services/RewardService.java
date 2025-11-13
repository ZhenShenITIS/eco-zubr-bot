package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.exceptions.NotEnoughPointsException;
import itis.ecozubrbot.exceptions.RewardSoldOutException;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.models.Reward;

import java.util.List;

public interface RewardService {

    List<Reward> getRewardsSortedByPoints();

    Reward getById(Long rewardId);

    Reward purchase(Long rewardId, Long userId) throws RewardSoldOutException, NotEnoughPointsException;

    void addReward(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException;
}
