package itis.ecozubrbot.config;

import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.services.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OtherConfig {

    private UploadService uploadService;

    @Bean
    public BasicFileMap basicFileMap() {
        return new BasicFileMap(uploadService);
    }
}
