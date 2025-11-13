package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
