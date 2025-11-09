package itis.ecozubrbot.max.containers;

import com.google.common.collect.ImmutableMap;
import itis.ecozubrbot.max.callbacks.Callback;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CallbackContainer {
    private final ImmutableMap<String, Callback> callbacks;

    public CallbackContainer(List<Callback> callbackList) {
        HashMap<String, Callback> map = new HashMap<>();
        for (Callback callback : callbackList) {
            map.put(callback.getCallback().getCallbackName(), callback);
        }
        callbacks = ImmutableMap.copyOf(map);
    }

    public Callback retrieveCallback(String callbackIdentifier) {
        return callbacks.get(callbackIdentifier);
    }
}
