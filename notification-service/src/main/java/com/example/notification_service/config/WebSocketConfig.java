package com.example.notification_service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker //This is us flipping the power switch to turn on the phone company inside our restaurant
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void  registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")//We are publishing our "phone number" in the phone book. We are telling the React frontend: "If you want to call us, dial the /ws extension."
        .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public  void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }



}
