package itis.ecozubrbot.repositories.impl;

import itis.ecozubrbot.repositories.UserEventOnModerationRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MapUserEventOnModerationRepository implements UserEventOnModerationRepository {

    final Map<Long, Long> map = new ConcurrentHashMap<>();

    @Override
    public void put(Long userId, Long userEventId) {
        map.put(userId, userEventId);
    }


    @Override
    public Long getUserEventId(Long userId) {
        return map.get(userId);
    }
}
