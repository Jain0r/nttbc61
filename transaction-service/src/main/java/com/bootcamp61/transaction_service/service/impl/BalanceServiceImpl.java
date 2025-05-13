package com.bootcamp61.transaction_service.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bootcamp61.transaction_service.repository.TransactionRepository;
import com.bootcamp61.transaction_service.service.BalanceService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{

    private final TransactionRepository repository;

    @Override
    public Mono<BigDecimal> getBalanceForProduct(String productId) {
        return repository.findByProductId(productId)
                         .map(transaction -> {
                            if (transaction.getType() == null || transaction.getAmount() == null) return BigDecimal.ZERO;
                            return switch (transaction.getType()){
                                case DEPOSIT, PAYMENT -> transaction.getAmount();
                                case WITHDRAWAL, CHARGE -> transaction.getAmount().negate();
                            };
                        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
}
