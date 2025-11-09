package itis.ecozubrbot.config;

import itis.ecozubrbot.max.containers.StateContainer;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.max.states.impl.DefaultState;
import itis.ecozubrbot.max.states.impl.TestState;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class StateConfig {
    private TestState testState;
    private DefaultState defaultState;
    private List<State> stateList;

    @Bean
    public StateContainer stateContainer() {
        stateList.add(testState);
        stateList.add(defaultState);
        return new StateContainer(stateList);
    }
}
