package itis.ecozubrbot.admincontrol;

import itis.ecozubrbot.max.EcoZubrBot;

public class AdminControl extends Thread {
    EcoZubrBot ecoZubrBot;

    public AdminControl(EcoZubrBot bot) {
        ecoZubrBot = bot;
    }

    @Override
    public void run() {
        // Команды для админки
    }
}
