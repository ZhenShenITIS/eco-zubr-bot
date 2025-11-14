package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IntegerConstants {
    COUNT_ELEMENTS_PER_PAGE(5),
    MAX_LENGTH_TEXT_OF_LIST_BUTTON(35),
    MIN_XP_FOR_SECOND_STAGE_OF_PET(1000),
    MIN_XP_FOR_THIRD_STAGE_OF_PET(2000);
    private final int value;
}
