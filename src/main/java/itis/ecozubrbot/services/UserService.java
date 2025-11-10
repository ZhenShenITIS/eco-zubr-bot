package itis.ecozubrbot.services;

import itis.ecozubrbot.models.User;

public interface UserService {
    void save(User user);

    User getById(Long id);
}
