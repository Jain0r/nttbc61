package com.bootcamp61.customer_service.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String name;

    private String dni;

    private CustomerType type;

    private List<String> holders;

    private List<String> signers;
    
}
