package itis.ecozubrbot.services;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserChallenge;

public interface UserChallengeService {
    UserChallenge getById(Long id);

    void save(UserChallenge userChallenge);

    Long createAndReturnId(Long userId, Long challengeId, TaskStatus status);
}
