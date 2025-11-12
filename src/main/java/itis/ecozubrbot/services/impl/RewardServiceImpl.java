package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Reward;
import itis.ecozubrbot.repositories.jpa.RewardRepository;
import itis.ecozubrbot.services.RewardService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RewardServiceImpl implements RewardService {
    private RewardRepository rewardRepository;

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
