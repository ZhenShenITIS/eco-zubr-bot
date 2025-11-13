package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallbackName {
    TEST("test"),
    PET("pet"),
    EMPTY("empty"),
    CHALLENGES("challenges"),
    SHOP("shop"),
    PROFILE("profile"),
    LEADERBOARD("leaderboard"),
    EVENTS("events"),
    CARESS("caress"),
    BACK_TO_MENU("back_to_menu"),
    REWARD_CARD("reward_card"),
    REWARD_PURCHASE("reward_purchase"),
    EVENT_CARD("event_card"),
    EVENT_DONE("event_done"),
    CHALLENGE_CARD("challenge_card"),
    CHALLENGE_DONE("challenge_done"),
    CHANGE_CITY("change_city"),
    EVENT_ACCEPT_FOR_SENDING_PROOF("event_accept_for_sending_proof"),
    CHALLENGE_ACCEPT_FOR_SENDING_PROOF("challenge_accept_for_sending_proof"),
    EDIT_PROOF_OF_CHALLENGE("edit_proof_of_challenge"),
    BACK_TO_PET_START("back_to_pet_start");

    private final String callbackName;
}
