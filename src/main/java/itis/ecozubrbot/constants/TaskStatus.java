package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskStatus {
    ACCEPTED("Принято"),
    REJECTED("Отклонено"),
    UNDER_REVIEW("На проверке");

    private final String value;
}
