package itis.ecozubrbot.max.states.newsletter_states;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

public class NewsletterCallback {
    public static void handlerCallback(MessageCallbackUpdate update, MaxClient client) {
        String type = update.getCallback().getPayload().split(":")[0];
        switch (type) {
            case "newsletterT": {
            }
        }
    }
}
