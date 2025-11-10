package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {}
