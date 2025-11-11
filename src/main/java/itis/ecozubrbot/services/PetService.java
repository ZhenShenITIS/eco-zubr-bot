package itis.ecozubrbot.services;

import itis.ecozubrbot.models.Pet;

public interface PetService {
    Pet getOrCreatePet(Long userId);
}
