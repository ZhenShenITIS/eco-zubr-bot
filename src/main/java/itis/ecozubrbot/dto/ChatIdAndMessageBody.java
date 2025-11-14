package itis.ecozubrbot.dto;

import ru.max.botapi.model.NewMessageBody;

public record ChatIdAndMessageBody(NewMessageBody newMessageBody, Long chat_id) {}
