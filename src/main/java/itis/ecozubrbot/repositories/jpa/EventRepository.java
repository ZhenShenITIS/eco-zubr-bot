package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
