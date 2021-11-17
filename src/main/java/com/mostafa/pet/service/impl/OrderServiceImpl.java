package com.mostafa.pet.service.impl;

import com.mostafa.pet.entity.Order;
import com.mostafa.pet.entity.Pet;
import com.mostafa.pet.exception.EntityAlreadyExist;
import com.mostafa.pet.exception.EntityNotFoundException;
import com.mostafa.pet.exception.PetIsNotAvailable;
import com.mostafa.pet.repository.OrderRepository;
import com.mostafa.pet.repository.PetRepository;
import com.mostafa.pet.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final PetRepository petRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(PetRepository petRepository, OrderRepository orderRepository) {
        this.petRepository = petRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order sellOrder(Order order) {

        if(order.getId() != null){
            throw new EntityAlreadyExist("this order already exist, your order cannot contain id.");
        }

        if(order.getPet() == null){
            throw new EntityNotFoundException("you should choose pet for this order you can not create order without pet.");
        }

        Optional<Pet> petOptional = petRepository.findById(order.getPet().getId());

        if(!petOptional.isPresent()){
            throw new EntityNotFoundException("there is no pet with this id, please check the id and try again");
        }

        Pet pet = petOptional.get();

        if(!pet.getAvailable()){
            throw new PetIsNotAvailable("this pet is already not available please choose another one.");
        }

        pet.setAvailable(false);
        Pet petResult = petRepository.save(pet);

        order.setPet(petResult);
        Order result = orderRepository.save(order);

        return result;
    }
}
