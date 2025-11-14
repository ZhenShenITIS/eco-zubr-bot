package itis.ecozubrbot.dto;

import ru.max.botapi.model.NewMessageBody;

public record ChatdAndMessageBody(NewMessageBody newMessageBody, Long chat_id) {}
