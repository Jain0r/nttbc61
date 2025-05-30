package com.bootcamp61.product_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.product_service.event.ProductCreatedEvent;
import com.bootcamp61.product_service.event.ProductEventPublisher;
import com.bootcamp61.product_service.listener.CustomerEventListener;
import com.bootcamp61.product_service.model.AccountType;
import com.bootcamp61.product_service.model.BankAccount;
import com.bootcamp61.product_service.model.CustomerType;
import com.bootcamp61.product_service.repository.BankAccountRepository;
import com.bootcamp61.product_service.service.BankAccountService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;

    private final ProductEventPublisher productEventPublisher;
    @Override
    public Flux<BankAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<BankAccount> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<BankAccount> create(BankAccount account) {
        CustomerType type = CustomerEventListener.customerTypeMap.get(account.getCustomerId());
        // CustomerType type = CustomerType.PERSONAL;
        // if(type == null)
        //     return Mono.error(new IllegalStateException("Tipo cliente no disponible"));
        
        AccountType accountType = account.getAccountType();

        //business no ahorro ni plazo fijo
        if(type == CustomerType.BUSINESS && accountType == AccountType.SAVINGS || accountType == AccountType.FIXED_TERM){
            return Mono.error(new IllegalStateException("Tipo de cuenta no permitido para cliente BUSINESS"));
        }

        //cliente persona solo 1 cuenta ppor tipo
        if(type == CustomerType.PERSONAL){
            return repository.findByCustomerId(account.getCustomerId())
                             .filter(existing -> existing.getAccountType() == accountType)
                             .doOnNext(existing -> System.out.println("Elemento encontrado:"  + existing.getCustomerId()))
                             .hasElements()
                             .flatMap(exists -> {
                                if(exists){
                                    return Mono.error(new IllegalStateException("Cliente personal solo puede tener una cuenta por tipo"));
                                } else {
                                    return repository.save(account);//.doOnSuccess(saved -> publishProductEvent(saved));
                                }
                             });
        }
        return repository.save(account);//.doOnSuccess(saved -> publishProductEvent(saved));
    }

    @Override
    public Mono<BankAccount> update(String id, BankAccount account) {
        return repository.findById(id)
                         .flatMap(existing -> {
                            // existing.setCustomerId(id);
                            existing.setAccountType(account.getAccountType());
                            existing.setBalance(account.getBalance());
                            existing.setHolders(account.getHolders());
                            existing.setAllowedMovementDate(account.getAllowedMovementDate());
                            existing.setMaxMonthlyMovements(account.getMaxMonthlyMovements());
                            existing.setMaintenanceFee(account.getMaintenanceFee());
                            return repository.save(existing);
                         });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
    
    private void publishProductEvent(BankAccount saved) {
    String allowedDate = saved.getAllowedMovementDate() != null
            ? saved.getAllowedMovementDate().toString()
            : null;

    productEventPublisher.publish(ProductCreatedEvent.builder()
            .productId(saved.getId())
            .productType(saved.getAccountType().name())
            .allowedMovementDate(allowedDate)
            .build());
}

}
