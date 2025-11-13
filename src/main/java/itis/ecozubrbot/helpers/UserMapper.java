package itis.ecozubrbot.helpers;

import itis.ecozubrbot.constants.UserRole;
import itis.ecozubrbot.models.User;

public class UserMapper {
    public static User getEntityFromMaxUser(ru.max.botapi.model.User user, Long chatId) {
        return User.builder()
                .id(user.getUserId())
                .chatId(chatId)
                .firstName(user.getFirstName())
                .role(UserRole.USER)
                .points(0)
                .build();
    }
}
