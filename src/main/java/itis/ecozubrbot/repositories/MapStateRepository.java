package itis.ecozubrbot.repositories;

import itis.ecozubrbot.constants.StateName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MapStateRepository implements StateRepository {

    final Map<Long, StateName> userStateMap = new ConcurrentHashMap<>();

    @Override
    public StateName get(Long userId) {
        return userStateMap.getOrDefault(userId, StateName.DEFAULT);
    }

    @Override
    public void put(Long userId, StateName state) {
        userStateMap.put(userId, state);
    }
}
