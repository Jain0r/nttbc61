package com.bootcamp61.customer_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp61.customer_service.model.Customer;
import com.bootcamp61.customer_service.service.CustomerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;

    @GetMapping()
    public Flux<Customer> getAll() {
        return customerService.findAll();
    }

    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getById(@PathVariable String id) {
        return customerService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create(@RequestBody Customer customer){
        Mono<Customer> obj = customerService.create(customer); 
        return obj;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> update(@PathVariable String id, @RequestBody Customer customer) {
        return customerService.update(id, customer).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        return customerService.findById(id)
                .flatMap(existing ->
                        customerService.delete(id)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());                                             
    }

}
