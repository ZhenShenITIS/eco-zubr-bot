package itis.ecozubrbot.max;

import itis.ecozubrbot.max.containers.StateContainer;
import itis.ecozubrbot.max.states.impl.TestState;
import itis.ecozubrbot.repositories.StateRepository;
import org.springframework.stereotype.Component;
import ru.max.bot.annotations.UpdateHandler;
import ru.max.bot.longpolling.LongPollingBot;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;

@Component
public class EcoZubrBot extends LongPollingBot {

    private TestState testState;
    private StateContainer stateContainer;
    private StateRepository stateRepository;

    public EcoZubrBot(
            String accessToken, TestState testState, StateContainer stateContainer, StateRepository stateRepository) {
        super(accessToken);
        this.testState = testState;
        this.stateContainer = stateContainer;
        this.stateRepository = stateRepository;
    }

    @UpdateHandler
    public void onMessageCreated(MessageCreatedUpdate update) {
        Thread.startVirtualThread(() -> stateContainer
                .getStateInstance(
                        stateRepository.get(update.getMessage().getSender().getUserId()))
                .handleMessageCreated(update, getClient()));
    }

    @UpdateHandler
    public void onMessageCallback(MessageCallbackUpdate update) {
        Thread.startVirtualThread(() -> stateContainer
                .getStateInstance(
                        stateRepository.get(update.getCallback().getUser().getUserId()))
                .handleMessageCallback(update, getClient()));
    }
}
