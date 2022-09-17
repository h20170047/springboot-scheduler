package com.svj.controller;

import com.svj.model.Order;
import com.svj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private OrderService service;

    @Autowired
    public OrderController(OrderService orderService){
        service= orderService;
    }

    @PostMapping("/orders")
    public Order saveOrder(@RequestBody Order order){
        return service.saveOrder(order);
    }
}
