package itis.ecozubrbot.max.containers;

import com.google.common.collect.ImmutableMap;
import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.services.UploadService;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BasicFileMap {

    private UploadService uploadService;

    private final ImmutableMap<BasicFile, String> files;

    public BasicFileMap(UploadService uploadService) {
        this.uploadService = uploadService;
        Map<BasicFile, String> map = new HashMap<>();
        for (BasicFile f : BasicFile.values()) {
            String token = this.uploadService.getImageToken(new File(f.getFilePath()));
            map.put(f, token);
        }
        files = ImmutableMap.copyOf(map);
    }

    public String getToken(BasicFile file) {
        return files.get(file);
    }
}
