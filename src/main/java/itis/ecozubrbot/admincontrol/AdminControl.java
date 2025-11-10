package itis.ecozubrbot.admincontrol;

import itis.ecozubrbot.max.EcoZubrBot;
import itis.ecozubrbot.newsletter.NewsletterManager;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.botapi.model.NewMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminControl extends Thread {
    EcoZubrBot ecoZubrBot;

    public AdminControl(EcoZubrBot ecoZubrBot) {
        this.ecoZubrBot = ecoZubrBot;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        NewsletterManager newsletterManager = new NewsletterManager(ecoZubrBot.getClient());
        while (true) {
            String command = sc.nextLine();
            switch (command) {
                case "1": {
                    long chat_id = 131743737;

                    NewMessageBody message = NewMessageBodyBuilder.ofText("Тебе пришла спам рассылка").build();
                    newsletterManager.sendMessage(message, chat_id);
                    break;
                }
                case "2": {

                }
            }
        }
    }

}
