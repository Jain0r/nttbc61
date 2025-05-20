package com.bootcamp61.transaction_service.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bootcamp61.transaction_service.event.ProductCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductEventListener {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Mapas en memoria
    public static final Map<String, String> productTypeMap = new ConcurrentHashMap<>();
    public static final Map<String, String> allowedMovementDateMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "product-events", groupId = "transaction-service-group")
    public void listen(String message) {
        try {
            ProductCreatedEvent event = objectMapper.readValue(message, ProductCreatedEvent.class);
            productTypeMap.put(event.getProductId(), event.getProductType());
            if ("FIXED_TERM".equals(event.getProductType())) {
                allowedMovementDateMap.put(event.getProductId(), event.getAllowedMovementDate());
            }
            log.info("Evento de producto recibido: {}", event);
        } catch (Exception e) {
            log.error("Error procesando evento de producto", e);
        }
    }
}
