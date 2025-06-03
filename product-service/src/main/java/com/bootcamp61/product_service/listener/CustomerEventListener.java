package com.bootcamp61.product_service.listener;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bootcamp61.product_service.model.CustomerCreatedEvent;
import com.bootcamp61.product_service.model.CustomerType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener{
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final ConcurrentHashMap<String, CustomerType> customerTypeMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "customer-events", groupId = "product-service-group")
    public void listen(String message){
        try{
            CustomerCreatedEvent event = objectMapper.readValue(message, CustomerCreatedEvent.class);
            // log
            customerTypeMap.put(event.getCustomerId(), event.getType());
        } catch (Exception e){
            
            // log e 
            //
        }
    }
}