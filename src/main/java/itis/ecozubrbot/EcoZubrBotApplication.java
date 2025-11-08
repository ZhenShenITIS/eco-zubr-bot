package itis.ecozubrbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class EcoZubrBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoZubrBotApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EcoZubrBot bot) {
        return args -> {
            bot.start();
        };
    }

}
