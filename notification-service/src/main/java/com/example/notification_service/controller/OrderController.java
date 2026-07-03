package com.example.notification_service.controller;


import com.example.notification_service.config.RabbitMQConfig;
import com.example.notification_service.entity.Order;
import com.example.notification_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private  final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;


    public  OrderController(OrderRepository orderRepository, RabbitTemplate rabbitTemplate){
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public  String createOrder(@RequestBody Order newOrder){
        //initial status shows as
        newOrder.setStatus("PENDING");

        //saved to postgres
        Order savedOrder=orderRepository.save(newOrder);

        String message="New Order Created! Id: "+savedOrder.getId()+", Product : "+ savedOrder.getProductName();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME,RabbitMQConfig.ROUTING_KEY,message);

        return  "✅ Order placed successfully and event published!";

    }

}
