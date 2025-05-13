package com.bootcamp61.customer_service.service;

import com.bootcamp61.customer_service.model.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    
    Flux<Customer> findAll();

    Mono<Customer> findById(String id);

    Mono<Customer> create(Customer customer);

    Mono<Customer> update(String id, Customer customer);

    Mono<Void> delete(String id);
}
