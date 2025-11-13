package itis.ecozubrbot.max.callbacks.impl.shop;

import itis.ecozubrbot.constants.CallbackName;
import itis.ecozubrbot.constants.StringConstants;
import itis.ecozubrbot.exceptions.NotEnoughPointsException;
import itis.ecozubrbot.exceptions.RewardSoldOutException;
import itis.ecozubrbot.max.callbacks.Callback;
import itis.ecozubrbot.models.Reward;
import itis.ecozubrbot.services.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.queries.SendMessageQuery;

@Component
@AllArgsConstructor
public class RewardPurchaseCallback implements Callback {
    private final CallbackName callbackName = CallbackName.REWARD_PURCHASE;
    private final RewardService rewardService;

    @Override
    public void handleMessageCallback(MessageCallbackUpdate update, MaxClient client) {
        Long rewardId = Long.parseLong(update.getCallback().getPayload().split(":")[1]);
        Long userId = update.getCallback().getUser().getUserId();
        NewMessageBody replyMessage;
        try {
            Reward reward = rewardService.purchase(rewardId, userId);
            replyMessage = NewMessageBodyBuilder.ofText(
                            StringConstants.SUCCESS_PURCHASE.getValue().formatted(reward.getValue()))
                    .build();
        } catch (RewardSoldOutException e) {
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.REWARD_SOLD_OUT.getValue())
                    .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                            CallbackName.SHOP.getCallbackName() + ":0", StringConstants.BACK_TO_SHOP.getValue()))))
                    .build();
        } catch (NotEnoughPointsException e) {
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.NOT_ENOUGH_POINTS.getValue())
                    .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                            CallbackName.SHOP.getCallbackName() + ":0", StringConstants.BACK_TO_SHOP.getValue()))))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            replyMessage = NewMessageBodyBuilder.ofText(StringConstants.ERROR.getValue())
                    .withAttachments(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(new CallbackButton(
                            CallbackName.SHOP.getCallbackName() + ":0", StringConstants.BACK_TO_SHOP.getValue()))))
                    .build();
        }

        SendMessageQuery query = new SendMessageQuery(client, replyMessage)
                .chatId(update.getMessage().getRecipient().getChatId());
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
