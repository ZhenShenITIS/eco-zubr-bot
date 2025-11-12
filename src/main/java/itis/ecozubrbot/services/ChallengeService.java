package itis.ecozubrbot.services;

import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.models.Challenge;
import java.util.List;

public interface ChallengeService {
    List<Challenge> getChallenges();

    Challenge getById(Long id);

    List<Challenge> getChallengesSortedByPoints();

    void addChallenge(String jsonChallenge, String photoToken) throws IncorrectJsonStringChallengeException;
}
