package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.repositories.jpa.ChallengeRepository;
import itis.ecozubrbot.services.ChallengeService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private ChallengeRepository challengeRepository;

    @Override
    public List<Challenge> getChallenges() {
        return List.of();
    }

    @Override
    public void addChallenge(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException {

        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);

            challengeRepository.save(Challenge.builder()
                    .title(jsonObject.getString("title"))
                    .description(jsonObject.getString("description"))
                    .experienceReward(jsonObject.getInt("experience_reward"))
                    .pointsReward(jsonObject.getInt("points_reward"))
                    .imageUrl(photoToken)
                    .build());
        } catch (Exception e) {
            throw new IncorrectJsonStringChallengeException();
        }
    }
}
