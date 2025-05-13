package com.bootcamp61.customer_service.event;

import com.bootcamp61.customer_service.model.CustomerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent {
    
    private String customerId;
    private CustomerType type;
}
