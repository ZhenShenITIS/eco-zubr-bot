package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {}
