package itis.ecozubrbot.max.states.newsletter_states;

import itis.ecozubrbot.service.newsletterwithtimer.challenge.ModerationChallengeFirstServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.MessageCallbackUpdate;

@Component
@AllArgsConstructor
public class NewsletterCallback {

    ModerationChallengeFirstServiceImpl moderationChallengeFirstService;

    public void handlerCallback(MessageCallbackUpdate update, MaxClient client) {
        String type = update.getCallback().getPayload().split(":")[0];
        switch (type) {
            case "newsletterT": {
                moderationChallengeFirstService.cameAnswer(update, client);
            }
        }
    }
}
