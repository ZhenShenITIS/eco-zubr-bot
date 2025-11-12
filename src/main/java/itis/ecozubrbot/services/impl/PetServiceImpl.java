package itis.ecozubrbot.services.impl;

import itis.ecozubrbot.models.Pet;
import itis.ecozubrbot.models.User;
import itis.ecozubrbot.repositories.jpa.PetRepository;
import itis.ecozubrbot.repositories.jpa.UserRepository;
import itis.ecozubrbot.services.PetService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    @Override
    public Pet getOrCreatePet(Long userId) {
        User userRef = userRepository.getReferenceById(userId);

        return petRepository.findById(userId).orElseGet(() -> {
            Pet pet = new Pet();
            pet.setUser(userRef);
            pet.setExperience(0);
            pet.setLastActionDate(LocalDate.now());
            return petRepository.save(pet);
        });
    }
}
