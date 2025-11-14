package itis.ecozubrbot.fillsql;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.exceptions.IncorrectJsonStringChallengeException;
import itis.ecozubrbot.max.containers.BasicFileMap;
import itis.ecozubrbot.services.ContentService;
import itis.ecozubrbot.services.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class FillSqlService {

    BasicFileMap basicFileMap;
    ContentService contentService;
    SetWithJson setWithJson;

    public void fillSql() throws IncorrectJsonStringChallengeException {
        String photoToken = basicFileMap.getToken(BasicFile.DEFAULT);
        HashSet<String> jsons = setWithJson.getSet();
        for(String json : jsons){
            contentService.add(json, photoToken);
        }
    }
}
