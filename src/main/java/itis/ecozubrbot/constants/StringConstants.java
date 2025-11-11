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
    BACK_BUTTON("Назад"),
    PROFILE_BUTTON("Профиль"),
    GEOLOCATION_BUTTON("Геолокация"),
    GEOLOCATION_UPDATE_SUCCESS("Геолокация успешно задана!"),
    GEOLOCATION_UPDATE_FAIL("Не удалось обновить геолокацию, попробуйте снова"),
    USER_NOT_FOUND("Мы не смогли определить вас. Скорее всего, вы не прошли процесс регистрации"),
    PET_START_MESSAGE("Это твой виртуальный зверёк!\n\nОпыт: %s"),
    CARESS_BUTTON("Приласкать"),
    CARESS_ANSWER("\uD83E\uDD7A\uD83D\uDC9E..."),
    BACK_TO_MENU_BUTTON("Вернуться в меню"),
    ADD_CONTENT_INFO("Отправь корректный json текст для добавления челленджа/события и прикрепи картинку"),
    ADD_CONTENT_SUCCESS("Успешно добавлено"),
    ADD_CONTENT_FAIL("Не удалось добавить, попробуйте ещё раз");
    private final String value;
}
