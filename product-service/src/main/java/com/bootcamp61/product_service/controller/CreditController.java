package com.bootcamp61.product_service.controller;

import com.bootcamp61.product_service.model.Credit;
import com.bootcamp61.product_service.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
 
    private final CreditService service;
 
    @GetMapping
    public Flux<Credit> findAll() {
        return service.findAll();
    }
 
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Credit>> findById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @GetMapping("/by-customer/{customerId}")
    public Flux<Credit> findByCustomerId(@PathVariable String customerId) {
        return service.findByCustomerId(customerId);
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Credit> create(@RequestBody Credit credit) {
        return service.create(credit);
    }
 
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Credit>> update(@PathVariable String id, @RequestBody Credit credit) {
        return service.update(id, credit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}