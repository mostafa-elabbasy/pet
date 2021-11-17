package com.mostafa.pet.service.impl;

import com.mostafa.pet.entity.Pet;
import com.mostafa.pet.exception.EntityAlreadyExist;
import com.mostafa.pet.exception.EntityNotFoundException;
import com.mostafa.pet.exception.PetIsNotAvailable;
import com.mostafa.pet.repository.PetRepository;
import com.mostafa.pet.service.PetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet createPet(Pet pet) {
        if(pet.getId() != null){
            throw new EntityAlreadyExist("this bet is already exist, you can update it instead");
        }

        pet.setAvailable(true);
        Pet result = petRepository.save(pet);

        return result;
    }

    @Override
    public Pet updatePet(Pet pet) {
        if (pet.getId() == null){
            throw new EntityNotFoundException("this is an invalid id, Id cannot be null.");
        }
        Optional<Pet> mainOptional = petRepository.findById(pet.getId());
        if(!mainOptional.isPresent()){
            throw new EntityNotFoundException("this is an invalid id, there is no pet with this id");
        }
        Pet main = mainOptional.get();
        if(!main.getAvailable()){
            throw new PetIsNotAvailable("you can not update this pet because this pet is not available now.");
        }
        main.setAge(pet.getAge());
        main.setBreed(pet.getBreed());
        main.setGender(pet.getGender());
        Pet result = petRepository.save(main);

        return result;
    }

    @Override
    public List<Pet> getPets() {
        return this.petRepository.findAll();
    }

    @Override
    public void deletePet(Long id) {
        if(id == null || id == 0){
            throw new EntityNotFoundException("this is an invalid id, Id cannot be null.");
        }
        Optional<Pet> petOptional = this.petRepository.findById(id);
        if(!petOptional.isPresent()){
            throw new EntityNotFoundException("this is an invalid id, there is no pet with this id");
        }
        Pet pet = petOptional.get();
        if(!pet.getAvailable()){
            throw new PetIsNotAvailable("you can not delete this pet because this pet is not available now.");
        }

        this.petRepository.delete(pet);
    }
}
