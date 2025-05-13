package com.bootcamp61.product_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp61.product_service.model.CreditCard;

import reactor.core.publisher.Flux;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    
    Flux<CreditCard> findByCustomerId(String customerId);//findByCustomerId(String customerId);
}
