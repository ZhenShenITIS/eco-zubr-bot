package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.services.ChallengeEventService;
import itis.ecozubrbot.services.ChallengeService;
import itis.ecozubrbot.services.EventService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChallengeEventServiceImpl implements ChallengeEventService {

    private final ChallengeService challengeService;
    private final EventService eventService;

    @Override
    public void add(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException {

        // проверяем прислал ли пользователь корректный json и определяем по его виду, что это событие или челлендж
        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);
            if (jsonObject.has("city")) {
                eventService.addEvent(jsonChallenge, photoToken);
            } else {
                challengeService.addChallenge(jsonChallenge, photoToken);
            }
        } catch (Exception e) {
            throw new IncorrectJsonStringChallengeException();
        }
    }
}
