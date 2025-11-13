package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserEvent;
import itis.ecozubrbot.repositories.jpa.EventRepository;
import itis.ecozubrbot.repositories.jpa.UserEventRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.UserEventService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEventServiceImpl implements UserEventService {
    private final UserEventRepository userEventRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public UserEvent getById(Long id) {
        return userEventRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(UserEvent userEvent) {
        if (userEvent.getUser() != null && userEvent.getUser().getId() != null) {
            Long uid = userEvent.getUser().getId();
            userEvent.setUser(userRepository.getReferenceById(uid));
        }
        if (userEvent.getEvent() != null && userEvent.getEvent().getId() != null) {
            Long eid = userEvent.getEvent().getId();
            userEvent.setEvent(eventRepository.getReferenceById(eid));
        }
        userEventRepository.save(userEvent);
    }

    @Override
    @Transactional
    public Long createAndReturnId(Long userId, Long eventId, TaskStatus status) {
        var ue = UserEvent.builder()
                .user(userRepository.getReferenceById(userId))
                .event(eventRepository.getReferenceById(eventId))
                .status(status)
                .build();
        ue = userEventRepository.saveAndFlush(ue);
        return ue.getId();
    }
}
