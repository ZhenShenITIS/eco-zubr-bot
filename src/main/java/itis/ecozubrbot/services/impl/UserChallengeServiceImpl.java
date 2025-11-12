package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.constants.TaskStatus;
import itis.ecozubrbot.models.UserChallenge;
import itis.ecozubrbot.repositories.jpa.ChallengeRepository;
import itis.ecozubrbot.repositories.jpa.UserChallengeRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.UserChallengeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChallengeServiceImpl implements UserChallengeService {
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public UserChallenge getById(Long id) {
        return userChallengeRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Long createAndReturnId(Long userId, Long challengeId, TaskStatus status) {
        var uc = UserChallenge.builder()
                .user(userRepository.getReferenceById(userId))
                .challenge(challengeRepository.getReferenceById(challengeId))
                .status(status)
                .build();
        uc = userChallengeRepository.saveAndFlush(uc);
        return uc.getId();
    }

    @Override
    @Transactional
    public void save(UserChallenge userChallenge) {
        if (userChallenge.getUser() != null && userChallenge.getUser().getId() != null) {
            Long uid = userChallenge.getUser().getId();
            userChallenge.setUser(userRepository.getReferenceById(uid));
        }
        if (userChallenge.getChallenge() != null && userChallenge.getChallenge().getId() != null) {
            Long cid = userChallenge.getChallenge().getId();
            userChallenge.setChallenge(challengeRepository.getReferenceById(cid));
        }
        userChallengeRepository.save(userChallenge);
    }
}
