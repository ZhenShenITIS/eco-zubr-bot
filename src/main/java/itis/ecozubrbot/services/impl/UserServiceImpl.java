package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
