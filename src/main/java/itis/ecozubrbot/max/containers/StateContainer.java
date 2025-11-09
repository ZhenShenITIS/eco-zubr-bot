package itis.ecozubrbot.max.containers;

import com.google.common.collect.ImmutableMap;
import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.max.states.State;
import java.util.HashMap;
import java.util.List;

import itis.ecozubrbot.max.states.impl.DefaultState;
import org.springframework.stereotype.Component;

@Component
public class StateContainer {

    private final ImmutableMap<StateName, State> states;

    public StateContainer(List<State> states) {
        HashMap<StateName, State> map = new HashMap<>();
        for (State state : states) {
            map.put(state.getState(), state);
        }
        this.states = ImmutableMap.copyOf(map);
    }

    public State getStateInstance(StateName stateName) {
        return states.get(stateName);
    }
}
