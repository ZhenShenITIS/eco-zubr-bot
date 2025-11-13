package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.EventRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.EventService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    @Override
    public List<Event> getEventsForUserSortedByPoints(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        String city = (user == null) ? "" : user.getCity();
        return eventRepository.findAllByCity(
                city,
                Sort.by("pointsReward")
                        .descending()
                        .and(Sort.by("experienceReward"))
                        .descending());
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
