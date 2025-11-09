package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StringConstants {
    UNKNOWN_COMMAND_ANSWER("Прости, я не знаю такой команды :("),
    HELP("Тут сообщение со справкой по использованию бота");
    private String value;
}
