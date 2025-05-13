package com.bootcamp61.transaction_service.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp61.transaction_service.model.Transaction;
import com.bootcamp61.transaction_service.service.BalanceService;
import com.bootcamp61.transaction_service.service.TransactionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService service;

    private final BalanceService balanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> create(@RequestBody Transaction transaction) {
        return service.create(transaction);
    }
 
    @GetMapping
    public Flux<Transaction> findAll() {
        return service.findAll();
    }
 
    @GetMapping("/by-product/{productId}")
    public Flux<Transaction> findByProductId(@PathVariable String productId) {
        return service.findByProductId(productId);
    }
 
    @GetMapping("/balance/{productId}")
    public Mono<BigDecimal> getBalance(@PathVariable String productId) {
        return balanceService.getBalanceForProduct(productId);
    }
 
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}
