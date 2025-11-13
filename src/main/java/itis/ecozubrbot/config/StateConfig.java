package itis.ecozubrbot.config;

import itis.ecozubrbot.max.containers.StateContainer;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.max.states.impl.DefaultState;
import itis.ecozubrbot.max.states.impl.GeolocationState;
import itis.ecozubrbot.max.states.impl.ImageUploadState;
import itis.ecozubrbot.max.states.impl.TestState;
import itis.ecozubrbot.max.states.impl.WaitingProofOfChallengeState;
import itis.ecozubrbot.max.states.impl.WaitingProofOfEventState;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class StateConfig {
    private TestState testState;
    private DefaultState defaultState;
    private GeolocationState geolocationState;
    private ImageUploadState imageUploadState;
    private WaitingProofOfChallengeState waitingProofOfChallengeState;
    private WaitingProofOfEventState waitingProofOfEventState;
    private List<State> stateList;

    @Bean
    public StateContainer stateContainer() {
        stateList.add(testState);
        stateList.add(defaultState);
        stateList.add(geolocationState);
        stateList.add(imageUploadState);
        stateList.add(waitingProofOfChallengeState);
        stateList.add(waitingProofOfEventState);
        return new StateContainer(stateList);
    }
}
