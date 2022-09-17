package com.svj.service;

import com.svj.model.Order;
import com.svj.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderService {
    private OrderRepository repository;

    @Autowired
    public void OrderService(OrderRepository orderRepository){
        repository= orderRepository;
    }

    @PostConstruct
    public void initDataToDB(){
        List<Order> orders = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> new Order("order-" + i, new Random().nextInt(5), new Random().nextDouble())).collect(Collectors.toList());
        repository.saveAll(orders);
    }

    public Order saveOrder(Order order){
        return repository.save(order);
    }
}
