package com.bootcamp61.product_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.product_service.model.Credit;
import com.bootcamp61.product_service.repository.CreditRepository;
import com.bootcamp61.product_service.service.CreditService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService{


    private final CreditRepository repository;

    @Override
    public Flux<Credit> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Credit> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Credit> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<Credit> create(Credit credit) {
        return repository.save(credit);
    }

    @Override
    public Mono<Credit> update(String id, Credit credit) {
        return repository.findById(id)
                         .flatMap(existing -> {
                            existing.setType(credit.getType());
                            existing.setTotalAmount(credit.getTotalAmount());
                            existing.setPaidAmount(credit.getPaidAmount());
                            existing.setStatus(credit.getStatus());
                            return repository.save(existing);
                         });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
    
}
