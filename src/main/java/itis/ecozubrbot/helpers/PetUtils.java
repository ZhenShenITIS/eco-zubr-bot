package itis.ecozubrbot.helpers;

import itis.ecozubrbot.constants.BasicFile;
import itis.ecozubrbot.constants.IntegerConstants;
import itis.ecozubrbot.max.containers.BasicFileMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PetUtils {

    private BasicFileMap basicFileMap;

    public String getImageOfPet(int xp) {
        return basicFileMap.getToken(
                getByXp(BasicFile.PET_THIRD_STAGE, BasicFile.PET_SECOND_STAGE, BasicFile.PET_FIRST_STAGE, xp));
    }

    public String getImageOfPetWithHearts(int xp) {
        return basicFileMap.getToken(getByXp(
                BasicFile.PET_THIRD_STAGE_HEARTS,
                BasicFile.PET_SECOND_STAGE_HEARTS,
                BasicFile.PET_FIRST_STAGE_HEARTS,
                xp));
    }

    private BasicFile getByXp(BasicFile f3, BasicFile f2, BasicFile f1, int xp) {
        if (xp >= IntegerConstants.MIN_XP_FOR_THIRD_STAGE_OF_PET.getValue()) {
            return f3;
        } else if (xp >= IntegerConstants.MIN_XP_FOR_SECOND_STAGE_OF_PET.getValue()) {
            return f2;
        } else {
            return f1;
        }
    }
}
