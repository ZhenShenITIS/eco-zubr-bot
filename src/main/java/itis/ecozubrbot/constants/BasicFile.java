package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BasicFile {
    PET_FIRST_STAGE("src/main/resources/static/pet_first_stage.png"),
    PET_FIRST_STAGE_HEARTS("src/main/resources/static/pet_first_stage_hearts.png"),
    PET_SECOND_STAGE("src/main/resources/static/pet_second_stage.png"),
    PET_SECOND_STAGE_HEARTS("src/main/resources/static/pet_second_stage_hearts.png"),
    PET_THIRD_STAGE("src/main/resources/static/pet_third_stage.png"),
    PET_THIRD_STAGE_HEARTS("src/main/resources/static/pet_third_stage_hearts.png");
    private final String filePath;
}
