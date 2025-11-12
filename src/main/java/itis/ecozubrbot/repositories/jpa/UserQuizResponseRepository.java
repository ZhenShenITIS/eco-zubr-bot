package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.UserQuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizResponseRepository extends JpaRepository<UserQuizResponse, Long> {
    long countByUserIdAndAnswer_IsCorrect(Long userId, boolean answerIsCorrect);

    long countByUser_Id(Long userId);
}
