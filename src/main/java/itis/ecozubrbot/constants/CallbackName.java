package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallbackName {
    TEST("test"),
    PET("pet"),
    TASKS("tasks"),
    SHOP("shop"),
    PROFILE("profile"),
    EVENTS("events"),
    CARESS("caress"),
    BACK_TO_MENU("back_to_menu"),
    BACK_TO_PET_START("back_to_pet_start");

    private final String callbackName;
}
