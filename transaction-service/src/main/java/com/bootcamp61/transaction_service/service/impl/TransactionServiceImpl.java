package com.bootcamp61.transaction_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.transaction_service.model.Transaction;
import com.bootcamp61.transaction_service.repository.TransactionRepository;
import com.bootcamp61.transaction_service.service.TransactionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository repository;

    @Override
    public Mono<Transaction> create(Transaction transaction) {
        transaction.setDate(java.time.LocalDateTime.now());
        return repository.save(transaction);
    }

    @Override
    public Flux<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Flux<Transaction> findByProductId(String productId) {
        return repository.findByProductId(productId);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    
}
