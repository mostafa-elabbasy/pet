package com.mostafa.pet.service;

import com.mostafa.pet.entity.Pet;

import java.util.List;

public interface PetService {

    public Pet createPet(Pet pet);

    public Pet updatePet(Pet pet);

    public List<Pet> getPets();

    public void deletePet(Long id);

}
