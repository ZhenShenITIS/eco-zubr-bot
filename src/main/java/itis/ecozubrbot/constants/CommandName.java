package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandName {
    MENU("/menu"),
    UNKNOWN("/"),
    HELP("/help"),
    ADD_CHALLENGE("/add_challenge");

    private final String commandName;
}
