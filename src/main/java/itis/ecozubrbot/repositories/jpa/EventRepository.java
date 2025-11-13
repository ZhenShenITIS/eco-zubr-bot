package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.Event;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCity(String city, Sort sort);
}
