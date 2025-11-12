package itis.ecozubrbot.repositories;

public interface UserChallengeOnModerationRepository {
    void put(Long userId, Long userChallengeId);

    Long getUserChallengeId(Long userId);
}
