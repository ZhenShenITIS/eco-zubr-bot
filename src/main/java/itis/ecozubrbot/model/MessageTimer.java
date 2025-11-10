package itis.ecozubrbot.model;

import itis.ecozubrbot.constants.NewsLetterTimerAnswer;
import itis.ecozubrbot.constants.NewsletterTimerStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class MessageTimer{
    private String uuid;
    private long newsletterId;
    private long chatId;
    private NewsletterTimerStatus status;
    private LocalDateTime timeSend;
    private LocalDateTime timeResponse;
    private NewsLetterTimerAnswer answer;

    public MessageTimer(long newsletterId, long chatId, NewsletterTimerStatus status, LocalDateTime timeSend) {
        this.newsletterId = newsletterId;
        this.chatId = chatId;
        this.status = status;
        this.timeSend = timeSend;
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "MessageTimer{" +
                "uuid='" + uuid + '\'' +
                ", newsletterId=" + newsletterId +
                ", chatId=" + chatId +
                ", status=" + status +
                ", timeSend=" + timeSend +
                ", timeResponse=" + timeResponse +
                ", answer=" + answer +
                '}';
    }
}
