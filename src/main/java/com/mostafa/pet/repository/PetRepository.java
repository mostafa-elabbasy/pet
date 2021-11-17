package com.mostafa.pet.repository;

import com.mostafa.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Repository
public interface PetRepository extends JpaRepository<Pet , Long> {
}
