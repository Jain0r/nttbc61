package com.bootcamp61.transaction_service.service;

import com.bootcamp61.transaction_service.model.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<Transaction> create(Transaction transaction);

    Flux<Transaction> findAll();

    Flux<Transaction> findByProductId(String productId);

    Mono<Void> delete(String id);
    
}
