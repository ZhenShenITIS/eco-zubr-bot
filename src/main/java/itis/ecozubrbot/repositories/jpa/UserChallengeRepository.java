package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {}
