package itis.ecozubrbot.config;

import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.callbacks.impl.BackToMenuCallback;
import itis.ecozubrbot.max.callbacks.impl.BackToPetStartCallback;
import itis.ecozubrbot.max.callbacks.impl.CaressCallback;
import itis.ecozubrbot.max.callbacks.impl.EmptyCallback;
import itis.ecozubrbot.max.callbacks.impl.event.EventAcceptProofForSendingCallback;
import itis.ecozubrbot.max.callbacks.impl.event.EventCardCallback;
import itis.ecozubrbot.max.callbacks.impl.event.EventDoneCallback;
import itis.ecozubrbot.max.callbacks.impl.event.EventsCallback;
import itis.ecozubrbot.max.callbacks.impl.PetStartCallback;
import itis.ecozubrbot.max.callbacks.impl.ProfileCallback;
import itis.ecozubrbot.max.callbacks.impl.shop.ShopCallback;
import itis.ecozubrbot.max.callbacks.impl.TestCallback;
import itis.ecozubrbot.max.callbacks.impl.challenge.ChallengeAcceptProofForSendingCallback;
import itis.ecozubrbot.max.callbacks.impl.challenge.ChallengeCardCallback;
import itis.ecozubrbot.max.callbacks.impl.challenge.ChallengeDoneCallback;
import itis.ecozubrbot.max.callbacks.impl.challenge.ChallengesCallback;
import itis.ecozubrbot.max.containers.CallbackContainer;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class CallbackConfig {
    private TestCallback testCallback;
    private EventsCallback eventsCallback;
    private ProfileCallback profileCallback;
    private ShopCallback shopCallback;
    private PetStartCallback tamagotchiStartCallback;
    private ChallengesCallback challengesCallback;
    private BackToMenuCallback backToMenuCallback;
    private BackToPetStartCallback backToPetStartCallback;
    private CaressCallback caressCallback;
    private ChallengeCardCallback challengeCardCallback;
    private ChallengeAcceptProofForSendingCallback challengeAcceptProofForSendingCallback;
    private ChallengeDoneCallback challengeDoneCallback;
    private EmptyCallback emptyCallback;
    private EventAcceptProofForSendingCallback eventAcceptProofForSendingCallback;
    private EventCardCallback eventCardCallback;
    private EventDoneCallback eventDoneCallback;
    private List<Callback> callbackList;

    @Bean
    public CallbackContainer callbackContainer() {
        callbackList.add(testCallback);
        callbackList.add(eventsCallback);
        callbackList.add(profileCallback);
        callbackList.add(shopCallback);
        callbackList.add(tamagotchiStartCallback);
        callbackList.add(challengesCallback);
        callbackList.add(backToMenuCallback);
        callbackList.add(backToPetStartCallback);
        callbackList.add(caressCallback);
        callbackList.add(challengeCardCallback);
        callbackList.add(challengeAcceptProofForSendingCallback);
        callbackList.add(challengeDoneCallback);
        callbackList.add(emptyCallback);
        callbackList.add(eventAcceptProofForSendingCallback);
        callbackList.add(eventCardCallback);
        callbackList.add(eventDoneCallback);
        return new CallbackContainer(callbackList);
    }
}
