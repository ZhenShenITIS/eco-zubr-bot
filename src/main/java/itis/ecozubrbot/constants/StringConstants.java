package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StringConstants {
    UNKNOWN_COMMAND_ANSWER("Прости, я не знаю такой команды :("),
    HELP("Тут сообщение со справкой по использованию бота"),
    START("Привет! Это бот эко-зубр. У нас ты можешь: "),
    START_BOT_UPDATE("Привет! Это бот эко-зубр. \n\nДля продолжения регистрации нам нужно узнать твой город"),
    PET_BUTTON("Зверёк"),
    TASKS_BUTTON("Задания"),
    EVENTS_BUTTON("События"),
    SHOP_BUTTON("Магазин"),
    PROFILE_BUTTON("Профиль"),
    GEOLOCATION_BUTTON("Геолокация"),
    GEOLOCATION_UPDATE_SUCCESS("Геолокация успешно задана!"),
    GEOLOCATION_UPDATE_FAIL("Не удалось обновить геолокацию, попробуйте снова");
    private String value;
}
