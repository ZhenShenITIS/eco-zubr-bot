package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import itis.ecozubrbot.models.Event;

import java.util.List;

public interface EventService {
    List<Event> getEvents();

    List<Event> getEventsForUserSortedByPoints(Long userId);

    Event getById(Long eventId);

    void addEvent(String jsonEvent, String photoToken) throws IncorrectJsonStringChallengeException;
}
