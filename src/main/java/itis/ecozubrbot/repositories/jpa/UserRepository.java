package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
