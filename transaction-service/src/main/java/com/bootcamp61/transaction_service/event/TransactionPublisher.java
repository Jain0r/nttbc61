package com.bootcamp61.transaction_service.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.bootcamp61.transaction_service.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String topic = "transaction-event";

    public void publishTransactionCreate(Transaction upd ){
        try{
            ProductTransactionUpdate event = ProductTransactionUpdate.builder()
                                                                     .idProduct(upd.getProductId())
                                                                     .newBalance(upd.getAmount())
                                                                     .build();
            String message = objectMapper.writeValueAsString(event);

            log.info("SE ENVIA POST TRANSACTION: " + upd.getId());

            kafkaTemplate.send(topic, message);
        } catch (Exception e){

        }
    }
}
