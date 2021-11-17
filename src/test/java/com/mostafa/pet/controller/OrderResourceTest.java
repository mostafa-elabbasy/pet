package com.mostafa.pet.controller;

import com.mostafa.pet.controller.util.TestUtil;
import com.mostafa.pet.entity.Order;
import com.mostafa.pet.entity.Pet;
import com.mostafa.pet.repository.OrderRepository;
import com.mostafa.pet.repository.PetRepository;
import org.aspectj.weaver.ast.Or;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderResourceTest {

    private static final Double DEFAULT_PRICE = 10.0;
    private static final Double UPDATED_PRICE = 20.0;

    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String UPDATED_CURRENCY = "DOLLAR";

    private static final String ENTITY_API_URL = "/api/order";

    @Autowired
    PetRepository petRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    public static Order createEntity(EntityManager em) {
        Order order = new Order();
        order.setCurrency(DEFAULT_CURRENCY);
        order.setPrice(DEFAULT_PRICE);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {

        Pet pet = PetResorceTest.createEntity(em);
        pet.setAvailable(true);
        em.persist(pet);
        em.flush();

        order.setPet(pet);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the order
        restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isCreated());

        // Validate the pet in the database
        List<Order> ordersList = orderRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = ordersList.get(ordersList.size() - 1);
        assertThat(testOrder.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getPet().getId()).isEqualTo(order.getPet().getId());
        assertThat(testOrder.getPet().getAvailable()).isEqualTo(false);
    }

    @Test
    @Transactional
    void createOrderWithNotNullId() throws Exception {

        Pet pet = PetResorceTest.createEntity(em);
        pet.setAvailable(true);
        em.persist(pet);
        em.flush();

        order.setPet(pet);

        order.setId(10L);

        // Create the order
        restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void createOrderWithPetNotAvailable() throws Exception {

        Pet pet = PetResorceTest.createEntity(em);
        pet.setAvailable(false);
        em.persist(pet);
        em.flush();

        order.setPet(pet);

        // Create the order
        restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void createOrderWithPetNotFound() throws Exception {

        Pet pet = PetResorceTest.createEntity(em);
        pet.setAvailable(true);
        em.persist(pet);
        em.flush();

        pet.setId(pet.getId() + 10);

        order.setPet(pet);

        // Create the order
        restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    void createOrderWithNullPet() throws Exception {

        order.setPet(null);

        // Create the order
        restOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isNotFound());

    }




}
