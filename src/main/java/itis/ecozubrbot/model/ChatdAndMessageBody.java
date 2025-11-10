package itis.ecozubrbot.model;

import ru.max.botapi.model.NewMessageBody;

public record ChatdAndMessageBody(NewMessageBody newMessageBody, Long chat_id){
}
