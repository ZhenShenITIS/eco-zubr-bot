package itis.ecozubrbot.max;

import itis.ecozubrbot.max.commands.impl.StartCommand;
import itis.ecozubrbot.max.containers.StateContainer;
import itis.ecozubrbot.max.handlers.BotStartedHandler;
import itis.ecozubrbot.max.states.impl.TestState;
import itis.ecozubrbot.repositories.StateRepository;
import org.springframework.stereotype.Component;
import ru.max.bot.annotations.UpdateHandler;
import ru.max.bot.longpolling.LongPollingBot;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.Update;

@Component
public class EcoZubrBot extends LongPollingBot {

    private TestState testState;
    private StateContainer stateContainer;
    private StateRepository stateRepository;
    private BotStartedHandler botStartedHandler;

    public EcoZubrBot(
            String accessToken, TestState testState, StateContainer stateContainer, StateRepository stateRepository, BotStartedHandler botStartedHandler) {
        super(accessToken);
        this.testState = testState;
        this.stateContainer = stateContainer;
        this.stateRepository = stateRepository;
        this.botStartedHandler = botStartedHandler;
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

    @UpdateHandler
    public void onBotStarted (BotStartedUpdate update) {
        Thread.startVirtualThread(() -> botStartedHandler.onBotStarted(update, getClient()));
    }

}
