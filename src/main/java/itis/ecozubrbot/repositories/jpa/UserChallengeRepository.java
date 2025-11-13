package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
