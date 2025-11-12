package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import java.util.List;

public interface EventService {
    List<Challenge> getEvents();

    void addEvent(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException;
}
