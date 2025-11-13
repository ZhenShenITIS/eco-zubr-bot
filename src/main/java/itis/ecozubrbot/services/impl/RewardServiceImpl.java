package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.exceptions.NotEnoughPointsException;
import itis.ecozubrbot.exceptions.RewardSoldOutException;
import itis.ecozubrbot.models.Reward;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.RewardRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.RewardService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RewardServiceImpl implements RewardService {
    private RewardRepository rewardRepository;
    private UserRepository userRepository;

    @Override
    public List<Reward> getRewardsSortedByPoints() {
        return rewardRepository.findAll(Sort.by("pointsCost")).stream().filter(r -> r.getAvailableQuantity() > 0).toList();
    }

    @Override
    public Reward getById(Long rewardId) {
        return rewardRepository.findById(rewardId).orElse(null);
    }

    @Override
    public Reward purchase(Long rewardId, Long userId) throws RewardSoldOutException, NotEnoughPointsException {
        Reward reward = rewardRepository.findById(rewardId).orElse(null);
        if (reward == null || reward.getAvailableQuantity() < 1) {
            throw new RewardSoldOutException(StringConstants.REWARD_SOLD_OUT.getValue());
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NullPointerException();
        }

        if (user.getPoints() < reward.getPointsCost()) {
            throw new NotEnoughPointsException(StringConstants.NOT_ENOUGH_POINTS.getValue());
        }

        user.setPoints(user.getPoints() - reward.getPointsCost());
        reward.setAvailableQuantity(reward.getAvailableQuantity() - 1);
        rewardRepository.save(reward);
        userRepository.save(user);
        return reward;
    }

    @Override
    public void addReward(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException {
        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);
            rewardRepository.save(Reward.builder()
                    .title(jsonObject.getString("title"))
                    .description(jsonObject.getString("description"))
                    .pointsCost(jsonObject.getInt("points_cost"))
                    .availableQuantity(jsonObject.getInt("available_quantity"))
                    .value(jsonObject.getString("value"))
                    .imageUrl(photoToken)
                    .build());
        } catch (Exception e) {
            throw new IncorrectJsonStringChallengeException();
        }
    }
}
