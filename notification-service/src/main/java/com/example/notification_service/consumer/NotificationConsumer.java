package com.example.notification_service.consumer;

import com.example.notification_service.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
public class NotificationConsumer {

    private  final SimpMessagingTemplate messagingTemplate;

    public NotificationConsumer(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate=messagingTemplate;
    }




    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME,ackMode = "MANUAL")
    public void consumeMessage(String message,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag ) throws IOException {

        try{
            if (message.toLowerCase().contains("salad")) {
                throw new RuntimeException("ERROR: The Kitchen Burned down!");
            }
            messagingTemplate.convertAndSend("/topic/notifications",message);
            channel.basicAck(deliveryTag,true);
        }
        catch(Exception e){
            System.out.println("❌ Error processing message: " + e.getMessage());
            channel.basicNack(deliveryTag,false ,false);
        }

       }
}
