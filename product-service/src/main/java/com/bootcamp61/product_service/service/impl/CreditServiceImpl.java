package com.bootcamp61.product_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.product_service.event.ProductCreatedEvent;
import com.bootcamp61.product_service.event.ProductEventPublisher;
import com.bootcamp61.product_service.listener.CustomerEventListener;

import com.bootcamp61.product_service.model.Credit;
import com.bootcamp61.product_service.model.CreditType;
import com.bootcamp61.product_service.model.CustomerType;
import com.bootcamp61.product_service.repository.CreditRepository;
import com.bootcamp61.product_service.service.CreditService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService{

    private final ProductEventPublisher productEventPublisher;


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
        CustomerType type = CustomerEventListener.customerTypeMap.get(credit.getCustomerId());

        if(type == null){
            return Mono.error(new IllegalStateException("Tipo de cliente no disponible"));
        }

        //validacion persona
        if (type == CustomerType.PERSONAL && credit.getType() == CreditType.PERSONAL){
            return repository.findByCustomerId(credit.getCustomerId())
                             .filter(existing -> existing.getType() == CreditType.PERSONAL)
                             .hasElements()
                             .flatMap(exists -> {
                                if(exists){
                                    return Mono.error(new IllegalStateException("Cliente PERSONAL sol puede tener un credito personal"));
                                } else {
                                    return repository.save(credit);
                                }

                             });
        }

        return repository.save(credit)
            .doOnSuccess(this::publishProductEvent);
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
    
    private void publishProductEvent(Credit saved) {
    productEventPublisher.publish(ProductCreatedEvent.builder()
            .productId(saved.getId())
            .productType("CREDIT")
            .allowedMovementDate(null)
            .build());
}
}
