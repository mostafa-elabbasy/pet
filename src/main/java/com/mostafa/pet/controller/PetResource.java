package com.mostafa.pet.controller;

import com.mostafa.pet.entity.Order;
import com.mostafa.pet.entity.Pet;
import com.mostafa.pet.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PetResource {

    private final PetService petService;

    public PetResource(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/pet")
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet){
        Pet result = petService.createPet(pet);

        return new ResponseEntity<>(result , HttpStatus.CREATED);
    }

    @PutMapping("/pet")
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet){
        Pet result = petService.updatePet(pet);

        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @GetMapping("/pet")
    public ResponseEntity<List<Pet>> getPets(){
        List<Pet> pets = petService.getPets();

        return new ResponseEntity<>(pets , HttpStatus.OK);
    }

    @DeleteMapping("/pet/{id}")
    public ResponseEntity<Void> getPets(@PathVariable Long id){
        petService.deletePet(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
