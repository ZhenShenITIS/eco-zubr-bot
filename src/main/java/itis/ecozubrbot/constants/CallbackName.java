package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallbackName {
    TEST("test");

    private final String callbackName;
}
