package itis.ecozubrbot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallbackName {
    TEST("test"),
    PET("pet"),
    CHALLENGES("challenges"),
    SHOP("shop"),
    PROFILE("profile"),
    EVENTS("events"),
    CARESS("caress"),
    BACK_TO_MENU("back_to_menu"),
    CHALLENGE_CARD("challenge_card"),
    CHALLENGE_DONE("challenge_done"),
    ACCEPT_FOR_SENDING_PROOF_OF_CHALLENGE("accept_for_sending_proof_of_challenge"),
    EDIT_PROOF_OF_CHALLENGE("edit_proof_of_challenge"),
    BACK_TO_PET_START("back_to_pet_start");

    private final String callbackName;
}
