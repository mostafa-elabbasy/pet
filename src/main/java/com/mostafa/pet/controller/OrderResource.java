package com.mostafa.pet.controller;

import com.mostafa.pet.entity.Order;
import com.mostafa.pet.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> sellOrder(@RequestBody Order order){
        Order result = orderService.sellOrder(order);

        return new ResponseEntity<>(result , HttpStatus.CREATED);
    }

}
