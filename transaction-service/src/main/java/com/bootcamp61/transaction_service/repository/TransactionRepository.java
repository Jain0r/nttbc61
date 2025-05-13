package com.bootcamp61.transaction_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp61.transaction_service.model.Transaction;

import reactor.core.publisher.Flux;


public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findByProductId(String productId);
    
}
