package com.bootcamp61.product_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp61.product_service.model.BankAccount;

import reactor.core.publisher.Flux;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String>{

    // Flux<BankAccount> findByCustomerId(String customerId);
    Flux<BankAccount> findByCustomerId(String customerId);    
}
