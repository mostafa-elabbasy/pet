package com.mostafa.pet.controller;

import com.mostafa.pet.controller.util.TestUtil;
import com.mostafa.pet.entity.Pet;
import com.mostafa.pet.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PetResorceTest {

    private static final String DEFAULT_BREED = "AAAAAAAAAA";
    private static final String UPDATED_BREED = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "MALE";
    private static final String UPDATED_GENDER = "FEMALE";

    private static final Integer DEFAULT_AGE = 10;
    private static final Integer UPDATED_AGE = 20;

    private static final String ENTITY_API_URL = "/api/pet";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    PetRepository petRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetMockMvc;

    private Pet pet;

    public static Pet createEntity(EntityManager em) {
        Pet pet = new Pet();
        pet.setBreed(DEFAULT_BREED);
        pet.setGender(DEFAULT_GENDER);
        pet.setAge(DEFAULT_AGE);

        return pet;
    }

    @BeforeEach
    public void initTest() {
        pet = createEntity(em);
    }

    @Test
    @Transactional
    void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();
        // Create the pet
        restPetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet)))
                .andExpect(status().isCreated());

        // Validate the pet in the database
        List<Pet> petsList = petRepository.findAll();
        assertThat(petsList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petsList.get(petsList.size() - 1);
        assertThat(testPet.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPet.getBreed()).isEqualTo(DEFAULT_BREED);
        assertThat(testPet.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPet.getAvailable()).isEqualTo(true);
    }

    @Test
    @Transactional
    void createPetWithExistingId() throws Exception {

        pet.setId(1L);

        // Create the pet
        restPetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void updatePet() throws Exception {

        // Initialize the database
        pet.setAvailable(true);
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);


        updatedPet.setBreed(UPDATED_BREED);
        updatedPet.setAge(UPDATED_AGE);
        updatedPet.setGender(UPDATED_GENDER);
        updatedPet.setAvailable(false);

        restPetMockMvc
                .perform(
                        put(ENTITY_API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedPet))
                )
                .andExpect(status().isOk());

        // Validate the pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testPet.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPet.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPet.getAvailable()).isEqualTo(true);

    }

    @Test
    @Transactional
    void updatePetWithNullId() throws Exception {

        // Update the pet
        Pet updatedPet = new Pet();

        updatedPet.setId(null);
        updatedPet.setBreed(UPDATED_BREED);
        updatedPet.setAge(UPDATED_AGE);
        updatedPet.setGender(UPDATED_GENDER);
        updatedPet.setAvailable(false);

        restPetMockMvc
                .perform(
                        put(ENTITY_API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedPet))
                )
                .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    void updatePetNotExisting() throws Exception {

        pet.setAvailable(true);
        petRepository.saveAndFlush(pet);

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();

        updatedPet.setId(updatedPet.getId() + 1000);
        updatedPet.setBreed(UPDATED_BREED);
        updatedPet.setAge(UPDATED_AGE);
        updatedPet.setGender(UPDATED_GENDER);
        updatedPet.setAvailable(false);

        restPetMockMvc
                .perform(
                        put(ENTITY_API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedPet))
                )
                .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    void updatePetNotAvailable() throws Exception {

        // Initialize the database
        pet.setAvailable(false);
        petRepository.saveAndFlush(pet);

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);

        updatedPet.setBreed(UPDATED_BREED);
        updatedPet.setAge(UPDATED_AGE);
        updatedPet.setGender(UPDATED_GENDER);
        updatedPet.setAvailable(false);

        restPetMockMvc
                .perform(
                        put(ENTITY_API_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedPet))
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void getAllPet() throws Exception {
        // Initialize the database
        pet.setAvailable(true);
        petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
                .andExpect(jsonPath("$.[*].breed").value(hasItem(DEFAULT_BREED)))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
                .andExpect(jsonPath("$.[*].available").value(hasItem(true)));
    }

    @Test
    @Transactional
    void deletePet() throws Exception {
        // Initialize the database
        pet.setAvailable(true);
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Delete the pet
        restPetMockMvc
                .perform(delete(ENTITY_API_URL_ID, pet.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Validate the database contains one less item
        List<Pet> expenseList = petRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void deletePetWithZeroOrNullId() throws Exception {
        // Initialize the database
        pet.setAvailable(true);
        petRepository.saveAndFlush(pet);

        // Delete the pet
        restPetMockMvc
                .perform(delete(ENTITY_API_URL_ID, 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    void deletePetNotFound() throws Exception {

        // Delete the pet
        restPetMockMvc
                .perform(delete(ENTITY_API_URL_ID, 100L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    void deletePetNotAvailable() throws Exception {
        // Initialize the database
        pet.setAvailable(false);
        petRepository.saveAndFlush(pet);

        // Delete the pet
        restPetMockMvc
                .perform(delete(ENTITY_API_URL_ID, pet.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
