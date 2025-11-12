package itis.ecozubrbot.repositories.impl;

import itis.ecozubrbot.repositories.UserChallengeOnModerationRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UserChallengeOnModerationRepositoryImpl implements UserChallengeOnModerationRepository {

    final Map<Long, Long> map = new ConcurrentHashMap<>();

    @Override
    public void put(Long userId, Long userChallengeId) {
        map.put(userId, userChallengeId);
    }

    @Override
    public void remove(Long userId) {
        map.remove(userId);
    }

    @Override
    public Long getUserChallengeId(Long userId) {
        return map.get(userId);
    }
}
