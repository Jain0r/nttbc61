package com.bootcamp61.product_service.service;

import com.bootcamp61.product_service.model.BankAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Flux<BankAccount> findAll();
    Mono<BankAccount> findById(String id);
    Flux<BankAccount> findByCustomerId(String customerId);
    Mono<BankAccount> create(BankAccount account);
    Mono<BankAccount> update(String id, BankAccount account);
    Mono<Void> delete(String id);
}
