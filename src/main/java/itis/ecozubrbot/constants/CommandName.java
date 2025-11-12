package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandName {
    MENU("/menu"),
    UNKNOWN("/"),
    HELP("/help"),
    ADD_CONTENT("/add_content");

    private final String commandName;
}
