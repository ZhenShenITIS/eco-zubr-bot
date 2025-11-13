package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.UserChallengeRepository;
import itis.ecozubrbot.repositories.jpa.UserEventRepository;
import itis.ecozubrbot.repositories.jpa.UserQuizResponseRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserEventRepository userEventRepository;
    private final UserQuizResponseRepository userQuizResponseRepository;

    @Override
    public String getProfileForUser(Long userId) {
        User userFromId = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("Пользователь " + userId + " должен существовать"));

        long countChallenges = userChallengeRepository.countByUserIdAndStatus(userId, TaskStatus.ACCEPTED);
        long countEvents = userEventRepository.countByUserIdAndStatus(userId, TaskStatus.ACCEPTED);
        long countQuiz = userQuizResponseRepository.countByUser_Id(userId);

        return StringConstants.PROFILE_INFO
                .getValue()
                .formatted(
                        userFromId.getFirstName(),
                        userFromId.getCity(),
                        userFromId.getCreatedDate().toString(),
                        userFromId.getPoints(),
                        userFromId.getPet().getExperience(),
                        countChallenges,
                        countEvents,
                        countQuiz);
    }
}
