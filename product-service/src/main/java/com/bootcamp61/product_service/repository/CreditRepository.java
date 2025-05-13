package com.bootcamp61.product_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp61.product_service.model.Credit;

import reactor.core.publisher.Flux;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String>{
    
    Flux<Credit> findByCustomerId(String customerId);

}
