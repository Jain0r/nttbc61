package com.bootcamp61.customer_service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.bootcamp61.customer_service.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String topic = "customer-events";

    public void publishCustomerCreated(Customer customer){
        try{
            System.out.println("Se solicito informacion proviente de: xxx");
            CustomerCreatedEvent event = CustomerCreatedEvent.builder()
                                                             .customerId(customer.getId())
                                                             .type(customer.getType())
                                                             .build();
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, message);
            // log
        } catch (Exception e){
            // log e 
        }
    }
}
