package com.bootcamp61.product_service.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp61.product_service.model.BankAccount;
import com.bootcamp61.product_service.service.BankAccountService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class BankAccountController {
 
    private final BankAccountService service;
 
    @GetMapping
    public Flux<BankAccount> findAll() {
        return service.findAll();
    }
 
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> findById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @GetMapping("/by-customer/{customerId}")
    public Flux<BankAccount> findByCustomerId(@PathVariable String customerId) {
        return service.findByCustomerId(customerId);
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BankAccount> create(@RequestBody BankAccount account) {
        return service.create(account);
    }
 
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> update(@PathVariable String id, @RequestBody BankAccount account) {
        return service.update(id, account)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}