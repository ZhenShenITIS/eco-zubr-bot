package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCity(String city, Sort sort);
}
