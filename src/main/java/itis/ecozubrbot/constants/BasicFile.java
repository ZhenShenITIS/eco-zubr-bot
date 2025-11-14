package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BasicFile {
    // ✅ ПРАВИЛЬНО: пути относительно classpath (src/main/resources)
    PET_FIRST_STAGE("static/pet_first_stage.png"),
    PET_FIRST_STAGE_HEARTS("static/pet_first_stage_hearts.png"),
    PET_SECOND_STAGE("static/pet_second_stage.png"),
    PET_SECOND_STAGE_HEARTS("static/pet_second_stage_hearts.png"),
    PET_THIRD_STAGE("static/pet_third_stage.png"),
    PET_THIRD_STAGE_HEARTS("static/pet_third_stage_hearts.png"),
    CHALLENGES("static/challenges.png"),
    EVENTS("static/events.png"),
    LEADERBOARD("static/leaderboard.png"),
    PROFILE("static/profile.png"),
    DEFAULT("static/default_image.png"),
    SHOP("static/shop.png");

    private final String filePath;
}
