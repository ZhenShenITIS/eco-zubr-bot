package itis.ecozubrbot;

import itis.ecozubrbot.admincontrol.AdminControl;
import itis.ecozubrbot.max.EcoZubrBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcoZubrBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoZubrBotApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EcoZubrBot bot) {
        return args -> {
            bot.start();
            new AdminControl(bot).start();
        };
    }
}
