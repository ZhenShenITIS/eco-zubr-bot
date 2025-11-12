package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.services.ContentService;
import itis.ecozubrbot.services.ChallengeService;
import itis.ecozubrbot.services.EventService;
import itis.ecozubrbot.services.RewardService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ChallengeService challengeService;
    private final EventService eventService;
    private final RewardService rewardService;

    @Override
    public void add(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException {

        // проверяем прислал ли пользователь корректный json и определяем по его виду, что это событие или челлендж
        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);
            if (jsonObject.has("city")) {
                eventService.addEvent(jsonChallenge, photoToken);
            } else if (jsonObject.has("points_cost")) {
                rewardService.addReward(jsonChallenge, photoToken);
            } else {
                challengeService.addChallenge(jsonChallenge, photoToken);
            }
        } catch (Exception e) {
            throw new IncorrectJsonStringChallengeException();
        }
    }
}
