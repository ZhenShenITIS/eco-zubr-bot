package itis.ecozubrbot.repositories;

import itis.ecozubrbot.dto.MessageTimer;
import java.util.List;

public interface MessageTimerRepository {
    List<MessageTimer> findByNewsLetterId(long newsletterId);

    void save(MessageTimer messageTimer);

    void update(MessageTimer messageTimer);

    void delete(MessageTimer messageTimer);
}
