package com.bootcamp61.product_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.product_service.event.ProductCreatedEvent;
import com.bootcamp61.product_service.event.ProductEventPublisher;
import com.bootcamp61.product_service.listener.CustomerEventListener;
import com.bootcamp61.product_service.model.CreditCard;
import com.bootcamp61.product_service.model.CustomerType;
import com.bootcamp61.product_service.repository.CreditCardRepository;
import com.bootcamp61.product_service.service.CreditCardService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService{

    private final CreditCardRepository repository;

    private final ProductEventPublisher productEventPublisher;

    @Override
    public Flux<CreditCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<CreditCard> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<CreditCard> create(CreditCard card) {
        CustomerType type = CustomerEventListener.customerTypeMap.get(card.getCustomerId());

        if (type == null) {
            return Mono.error(new IllegalStateException("Tipo de cliente no disponible para ID: " + card.getCustomerId()));
        }

        if (card.getUsedAmount() != null && card.getCreditLimit() != null &&
            card.getUsedAmount().compareTo(card.getCreditLimit()) > 0) {
            return Mono.error(new IllegalStateException("El monto usado no puede exceder el límite de crédito"));
        }

        return repository.save(card)
            .doOnSuccess(this::publishProductEvent);
    }

    @Override
    public Mono<CreditCard> update(String id, CreditCard creditCard) {
        return repository.findById(id)
                         .flatMap(existing -> {
                            existing.setType(creditCard.getType());
                            existing.setCreditLimit(creditCard.getCreditLimit());
                            existing.setUsedAmount(creditCard.getUsedAmount());
                            existing.setStatus(creditCard.getStatus());
                            existing.setCardNumber(creditCard.getCardNumber());
                            existing.setExpirationDate(creditCard.getExpirationDate());
                            existing.setCvv(creditCard.getCvv());
                            existing.setNameOnCard(creditCard.getNameOnCard());
                            return repository.save(existing);
                         });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
    
    private void publishProductEvent(CreditCard saved) {
        productEventPublisher.publish(ProductCreatedEvent.builder()
                .productId(saved.getId())
                .productType("CREDIT_CARD")
                .allowedMovementDate(null)
                .build());
    }
}
