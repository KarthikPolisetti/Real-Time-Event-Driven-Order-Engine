package com.example.notification_service.runner;

import com.example.notification_service.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MessageRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public  MessageRunner(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;
    }

    @Override
    public void run(String... args) throws  Exception{
        String message="Hello from spring boot ! This is my first event!";

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,message);
        System.out.println("✅ Message sent to RabbitMQ successfully! ");
    }
}
