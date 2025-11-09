package itis.ecozubrbot.config;

import itis.ecozubrbot.max.containers.StateContainer;
import itis.ecozubrbot.max.states.DefaultState;
import itis.ecozubrbot.max.states.State;
import itis.ecozubrbot.max.states.TestState;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class ContainersConfig {

    private DefaultState defaultState;
    private TestState testState;
    private List<State> stateList;

    @Bean
    public StateContainer stateContainer() {
        stateList.add(defaultState);
        stateList.add(testState);
        return new StateContainer(stateList);
    }
}
