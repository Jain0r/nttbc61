package com.bootcamp61.product_service.service;

import com.bootcamp61.product_service.model.CreditCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Flux<CreditCard> findAll();
    Mono<CreditCard> findById(String id);
    Flux<CreditCard> findByCustomerId(String customerId);
    Mono<CreditCard> create(CreditCard creditCard);
    Mono<CreditCard> update(String id, CreditCard creditCard);
    Mono<Void> delete(String id);
}
