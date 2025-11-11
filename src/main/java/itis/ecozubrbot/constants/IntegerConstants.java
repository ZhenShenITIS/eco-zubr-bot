package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntegerConstants {
    MIN_XP_FOR_SECOND_STAGE_OF_PET(1000),
    MIN_XP_FOR_THIRD_STAGE_OF_PET(2000);
    private final int value;
}
