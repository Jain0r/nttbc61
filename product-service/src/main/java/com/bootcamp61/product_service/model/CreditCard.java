package com.bootcamp61.product_service.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "credit_cards")
public class CreditCard {

    @Id
    private String id;
    
    private String customerId;

    private CreditType type;

    private BigDecimal creditLimit;

    private BigDecimal usedAmount;

    private String status;


    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private String nameOnCard;
}
