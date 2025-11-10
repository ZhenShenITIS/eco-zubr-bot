package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NewsletterTimerStatus {
    WAITING("waiting"),
    REPLIED("replied"),
    TIMEOUT("timeout");
    private final String value;
}
