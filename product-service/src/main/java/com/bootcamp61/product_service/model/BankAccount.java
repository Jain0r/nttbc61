package com.bootcamp61.product_service.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
@Document(collection = "bank_accounts")
public class BankAccount {
    
    @Id
    private String id;

    private String customerId;

    private AccountType accountType;

    private BigDecimal balance;

    private List<String> holders; //titulares Emp

    private List<String> authorizedSigners;

    private LocalDate allowedMovementDate; //aplica a cuentas a plazo fijo

    private Integer maxMonthlyMovements; //aplica a ahorros

    private BigDecimal maintenanceFee; //cuenta corriente
}
