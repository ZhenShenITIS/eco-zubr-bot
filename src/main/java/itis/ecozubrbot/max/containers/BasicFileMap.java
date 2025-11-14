package itis.ecozubrbot.max.containers;

import com.google.common.collect.ImmutableMap;
import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.services.UploadService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BasicFileMap {

    private UploadService uploadService;
    private final ImmutableMap<BasicFile, String> files;

    public BasicFileMap(UploadService uploadService) {
        this.uploadService = uploadService;
        Map<BasicFile, String> map = new HashMap<>();

        for (BasicFile f : BasicFile.values()) {
            try {
                // ✅ ИСПРАВЛЕНИЕ: работаем с InputStream вместо File
                ClassPathResource resource = new ClassPathResource(f.getFilePath());

                // Если файл в JAR, создаём временный файл
                InputStream inputStream = resource.getInputStream();
                File tempFile = File.createTempFile("eco-zubr-", ".png");
                tempFile.deleteOnExit(); // Удалить при выходе

                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    inputStream.transferTo(outputStream);
                }

                String token = this.uploadService.getImageToken(tempFile);
                map.put(f, token);

                log.info("Загружен файл: {}, token: {}", f.getFilePath(), token);
            } catch (IOException e) {
                log.error("Ошибка при загрузке файла {}: {}", f.getFilePath(), e.getMessage());
                throw new RuntimeException("Не удалось инициализировать BasicFileMap: " + e.getMessage(), e);
            }
        }

        files = ImmutableMap.copyOf(map);
    }

    public String getToken(BasicFile file) {
        return files.get(file);
    }
}
