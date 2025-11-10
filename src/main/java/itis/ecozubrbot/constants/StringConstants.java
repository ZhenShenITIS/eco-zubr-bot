package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StringConstants {
    UNKNOWN_COMMAND_ANSWER("Прости, я не знаю такой команды :("),
    HELP("Тут сообщение со справкой по использованию бота"),
    START("Привет! Это бот эко-зубр. У нас ты можешь: "),
    TAMAGOTCHI_BUTTON("Тамагочи"),
    TASKS_BUTTON("Задания"),
    EVENTS_BUTTON("События"),
    SHOP_BUTTON("Магазин"),
    PROFILE_BUTTON("Профиль");
    private String value;
}
