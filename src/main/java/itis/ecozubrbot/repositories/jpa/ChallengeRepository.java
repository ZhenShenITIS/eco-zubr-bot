package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {}
