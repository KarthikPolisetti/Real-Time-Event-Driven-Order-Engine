package com.example.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public  static final String QUEUE_NAME="notification.queue";

public static final String EXCHANGE_NAME="restaurant.exchange";

public static final String ROUTING_KEY="order.created";

public static  final String DLQ_NAME="notification.dlq";
@Bean
    public Queue myQueue(){
    return org.springframework.amqp.core.QueueBuilder.durable(QUEUE_NAME)
            .withArgument("x-dead-letter-exchange", "") // We use the default exchange
            .withArgument("x-dead-letter-routing-key", DLQ_NAME) // Route broken tickets here!
            .build();
}

@Bean
    public  TopicExchange myExchange(){
    return new TopicExchange(EXCHANGE_NAME);
}


@Bean
    public Binding myRouting(){
    return BindingBuilder.bind(myQueue()).to(myExchange()).with(ROUTING_KEY);
}

@Bean
    public Queue deadLetterQueue(){
    return new Queue(DLQ_NAME,true);
}



}
