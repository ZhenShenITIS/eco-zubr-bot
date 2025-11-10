package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {}
