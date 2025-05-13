package com.bootcamp61.customer_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bootcamp61.customer_service.model.Customer;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Flux<Customer> findByType(String type);
}
    
