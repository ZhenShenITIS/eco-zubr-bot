package itis.ecozubrbot.services;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.models.UserEvent;

public interface UserEventService {
    UserEvent getById(Long id);

    void save(UserEvent userEvent);

    Long createAndReturnId(Long userId, Long eventId, TaskStatus status);
}
