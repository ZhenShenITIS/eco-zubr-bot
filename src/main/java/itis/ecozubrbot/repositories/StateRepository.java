package itis.ecozubrbot.repositories;

import itis.ecozubrbot.constants.StateName;

public interface StateRepository {
    StateName get(Long userId);

    void put(Long userId, StateName state);
}
