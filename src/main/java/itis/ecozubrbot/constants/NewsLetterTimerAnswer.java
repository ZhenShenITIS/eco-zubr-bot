package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NewsLetterTimerAnswer {
    APPROVED("approved"),
    REJECTED("rejected");
    private final String value;
}
