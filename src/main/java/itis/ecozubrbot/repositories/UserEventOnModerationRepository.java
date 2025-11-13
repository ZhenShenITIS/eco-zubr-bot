package itis.ecozubrbot.repositories;

public interface UserEventOnModerationRepository {
    void put(Long userId, Long userEventId);

    Long getUserEventId(Long userId);

    void remove(Long userId);
}
