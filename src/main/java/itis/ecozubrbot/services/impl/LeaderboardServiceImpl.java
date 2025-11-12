package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.LeaderboardService;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {
    private final UserRepository userRepository;
    private final int USER_COUNT_IN_LEADERBOARD = 5;
    private final Map<Integer, String> leaderPlaceString = initiateLeaderPlaceString();

    @Override
    public String getLeaderboardForUser(Long userId) {
        List<User> topByExperience = userRepository.findTopByPetExperienceDesc(PageRequest.of(0, 5));
        Integer userPlace = userRepository.findRankByUserId(userId);
        User userFromId = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("Пользователь " + userId + " должен существовать"));

        StringBuilder sb = new StringBuilder();

        sb.append(StringConstants.LEADERBOARD_LIST_INFO.getValue()).append("\n");

        for (int i = 1; i < topByExperience.size() + 1; i++) {
            User nowUser = topByExperience.get(i - 1);
            sb.append(leaderPlaceString
                    .get(i)
                    .formatted(nowUser.getFirstName(), nowUser.getPet().getExperience()));
            sb.append("\n");
        }

        if (userPlace > USER_COUNT_IN_LEADERBOARD) {
            sb.append(StringConstants.LEADERBOARD_YOUR_PLACE
                    .getValue()
                    .formatted(
                            userPlace,
                            userFromId.getFirstName(),
                            userFromId.getPet().getExperience()));
        }

        return sb.toString();
    }

    private Map<Integer, String> initiateLeaderPlaceString() {
        Map<Integer, String> res = new HashMap<>();
        res.put(1, StringConstants.LEADERBOARD_1_PLACE.getValue());
        res.put(2, StringConstants.LEADERBOARD_2_PLACE.getValue());
        res.put(3, StringConstants.LEADERBOARD_3_PLACE.getValue());
        res.put(4, StringConstants.LEADERBOARD_4_PLACE.getValue());
        res.put(5, StringConstants.LEADERBOARD_5_PLACE.getValue());
        return res;
    }
}
