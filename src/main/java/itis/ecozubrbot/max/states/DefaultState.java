package itis.ecozubrbot.max.states;

import itis.ecozubrbot.constants.StateName;
import itis.ecozubrbot.repositories.StateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;

@AllArgsConstructor
@Component
public class DefaultState implements State {
    private final StateName state = StateName.DEFAULT;

    private StateRepository stateRepository;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {}

    @Override
    public void handleMessageCreated(MessageCreatedUpdate update, MaxClient client) {
        stateRepository.put(update.getMessage().getSender().getUserId(), StateName.TEST);
    }

    @Override
    public StateName getState() {
        return state;
    }
}
