package itis.ecozubrbot.max.callbacks.impl.event;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.IntegerConstants;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.models.Event;
import itis.ecozubrbot.services.EventService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.EditMessageQuery;

@Component
@AllArgsConstructor
public class EventsCallback implements Callback {
    private final CallbackName callbackName = CallbackName.EVENTS;
    private final EventService eventService;
    private BasicFileMap basicFileMap;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long userId = update.getCallback().getUser().getUserId();
        int nextIndex = Integer.parseInt(update.getCallback().getPayload().split(":")[1]);
        nextIndex = Math.max(nextIndex, 0);
        List<Event> events = eventService.getEventsForUserSortedByPoints(userId);

        List<List<Button>> layout = new ArrayList<>();
        int countOfIncrease = 0;
        int i;
        int bounds =
                nextIndex + Math.min(IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue(), events.size() - nextIndex);
        for (i = nextIndex; i < bounds; i++, countOfIncrease++) {
            List<Button> row = new ArrayList<>();
            Event event = events.get(i);
            String text = event.getTitle() + " | " + event.getPointsReward() + "/" + event.getExperienceReward();
            int maxLen = IntegerConstants.MAX_LENGTH_TEXT_OF_LIST_BUTTON.getValue();
            if (text.length() > maxLen) {
                int indexFin = event.getTitle().length() - 4 - (text.length() - maxLen);
                indexFin = Math.max(indexFin, 0);
                text = event.getTitle().substring(0, indexFin) + "... | " + event.getPointsReward() + "/"
                        + event.getExperienceReward();
            }
            row.add(new CallbackButton(
                    CallbackName.EVENT_CARD.getCallbackName() + ":" + event.getId() + ":" + nextIndex, text));
            layout.add(row);
        }
        List<Button> arrowRow = new ArrayList<>();
        arrowRow.add(new CallbackButton(
                CallbackName.EVENTS.getCallbackName() + ":"
                        + (i - IntegerConstants.COUNT_ELEMENTS_PER_PAGE.getValue() - countOfIncrease),
                StringConstants.BACKWARD_LIST_BUTTON.getValue()));

        arrowRow.add(new CallbackButton(CallbackName.EMPTY.getCallbackName(), StringConstants.VOID.getValue()));

        CallbackName forwardCallback = (i == events.size()) ? CallbackName.EMPTY : callbackName.EVENTS;

        arrowRow.add(new CallbackButton(
                forwardCallback.getCallbackName() + ":" + i, StringConstants.FORWARD_LIST_BUTTON.getValue()));
        layout.add(arrowRow);
        List<Button> backButton = List.of(new CallbackButton(
                CallbackName.BACK_TO_MENU.getCallbackName(), StringConstants.BACK_TO_MENU_BUTTON.getValue()));
        layout.add(backButton);

        NewMessageBody replyMessage = NewMessageBodyBuilder.ofText(StringConstants.EVENTS.getValue())
                .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.layout(layout))
                        .with(AttachmentsBuilder.photos(basicFileMap.getToken(BasicFile.EVENTS))))
                .build();
        EditMessageQuery query = new EditMessageQuery(
                client, replyMessage, update.getMessage().getBody().getMid());
        try {
            query.enqueue();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CallbackName getCallback() {
        return callbackName;
    }
}
