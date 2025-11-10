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
    EVENTS("events");

    private final String callbackName;
}
