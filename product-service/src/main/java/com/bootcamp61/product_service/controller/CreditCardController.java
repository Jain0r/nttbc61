package com.bootcamp61.product_service.controller;

import com.bootcamp61.product_service.model.CreditCard;
import com.bootcamp61.product_service.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@RestController
@RequestMapping("/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {
 
    private final CreditCardService service;
 
    @GetMapping
    public Flux<CreditCard> findAll() {
        return service.findAll();
    }
 
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CreditCard>> findById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @GetMapping("/by-customer/{customerId}")
    public Flux<CreditCard> findByCustomerId(@PathVariable String customerId) {
        return service.findByCustomerId(customerId);
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> create(@RequestBody CreditCard card) {
        return service.create(card);
    }
 
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreditCard>> update(@PathVariable String id, @RequestBody CreditCard card) {
        return service.update(id, card)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
 
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}