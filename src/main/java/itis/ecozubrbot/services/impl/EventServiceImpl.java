package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.repositories.jpa.EventRepository;
import itis.ecozubrbot.services.EventService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<Challenge> getEvents() {
        return List.of();
    }

    @Override
    public void addEvent(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException {
        try {
            JSONObject jsonObject = new JSONObject(jsonChallenge);

            eventRepository.save(Event.builder()
                    .city(jsonObject.getString("city"))
                    .endDateTime(LocalDateTime.parse(jsonObject.getString("end_date_time")))
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
