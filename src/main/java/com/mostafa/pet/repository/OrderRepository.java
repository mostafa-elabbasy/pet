package com.mostafa.pet.repository;

import com.mostafa.pet.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
