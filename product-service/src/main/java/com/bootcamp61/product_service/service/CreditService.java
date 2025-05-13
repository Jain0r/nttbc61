package com.bootcamp61.product_service.service;

import com.bootcamp61.product_service.model.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<Credit> findAll();
    Mono<Credit> findById(String id);
    Flux<Credit> findByCustomerId(String customerId);
    Mono<Credit> create(Credit credit);
    Mono<Credit> update(String id, Credit credit);
    Mono<Void> delete(String id);
}
