package itis.ecozubrbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class BotConfig {

    @Value("${MAX_TOKEN}")
    private String MAX_TOKEN;

    @Bean
    public String accessToken () {
        return MAX_TOKEN;
    }
}
