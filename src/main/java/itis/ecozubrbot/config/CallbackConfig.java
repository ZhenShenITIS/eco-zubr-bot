package itis.ecozubrbot.config;

import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.callbacks.impl.BackToMenuCallback;
import itis.ecozubrbot.max.callbacks.impl.BackToPetStartCallback;
import itis.ecozubrbot.max.callbacks.impl.CaressCallback;
import itis.ecozubrbot.max.callbacks.impl.EventsCallback;
import itis.ecozubrbot.max.callbacks.impl.PetStartCallback;
import itis.ecozubrbot.max.callbacks.impl.ProfileCallback;
import itis.ecozubrbot.max.callbacks.impl.ShopCallback;
import itis.ecozubrbot.max.callbacks.impl.TasksCallback;
import itis.ecozubrbot.max.callbacks.impl.TestCallback;
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
    private TasksCallback tasksCallback;
    private BackToMenuCallback backToMenuCallback;
    private BackToPetStartCallback backToPetStartCallback;
    private CaressCallback caressCallback;
    private List<Callback> callbackList;

    @Bean
    public CallbackContainer callbackContainer() {
        callbackList.add(testCallback);
        callbackList.add(eventsCallback);
        callbackList.add(profileCallback);
        callbackList.add(shopCallback);
        callbackList.add(tamagotchiStartCallback);
        callbackList.add(tasksCallback);
        callbackList.add(backToMenuCallback);
        callbackList.add(backToPetStartCallback);
        callbackList.add(caressCallback);
        return new CallbackContainer(callbackList);
    }
}
