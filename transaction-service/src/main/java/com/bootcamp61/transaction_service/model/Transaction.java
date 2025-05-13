package com.bootcamp61.transaction_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Document(collection = "transactions")
public class Transaction {
    
    @Id
    private String id;

    private String productId; //account, card, credit

    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime date;

    private String descripcion;
}
