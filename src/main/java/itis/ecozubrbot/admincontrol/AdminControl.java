package itis.ecozubrbot.admincontrol;

import itis.ecozubrbot.max.EcoZubrBot;
import itis.ecozubrbot.newsletter.NewsletterManager;
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
            String input = sc.nextLine();
            switch (input) {
                    // Команды для админки
            }
        }
    }
}
