package itis.ecozubrbot.config;

import itis.ecozubrbot.max.callbacks.Callback;
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
    private List<Callback> callbackList;

    @Bean
    public CallbackContainer callbackContainer() {
        callbackList.add(testCallback);
        return new CallbackContainer(callbackList);
    }
}
