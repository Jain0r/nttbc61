package com.bootcamp61.transaction_service.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bootcamp61.transaction_service.listener.ProductEventListener;
import com.bootcamp61.transaction_service.model.Transaction;
import com.bootcamp61.transaction_service.model.TransactionType;
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
    public Mono<Transaction> create(Transaction tx) {
        return repository.findByProductId(tx.getProductId())
            .map(existingTx -> {
                BigDecimal value;

                if (existingTx.getType() == null || existingTx.getAmount() == null) {
                    value = BigDecimal.ZERO;
                } else {
                    switch (existingTx.getType()) {
                        case DEPOSIT:
                        case PAYMENT:
                            value = existingTx.getAmount();
                            break;
                        case WITHDRAWAL:
                        case CHARGE:
                            value = existingTx.getAmount().negate();
                            break;
                        default:
                            value = BigDecimal.ZERO;
                    }
                }

                return value;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .flatMap(currentBalance -> {
                BigDecimal newBalance;

                switch (tx.getType()) {
                    case DEPOSIT:
                    case PAYMENT:
                        newBalance = currentBalance.add(tx.getAmount());
                        break;
                    case WITHDRAWAL:
                    case CHARGE:
                        newBalance = currentBalance.subtract(tx.getAmount());
                        break;
                    default:
                        return Mono.error(new IllegalArgumentException("Tipo de transacción no soportado"));
                }

                // 🔒 Validación de saldo negativo
                if (tx.getType() == TransactionType.WITHDRAWAL && newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    return Mono.error(new IllegalStateException("Saldo insuficiente para el retiro"));
                }

                // 🔒 Validación de fecha para FIXED_TERM
                String type = ProductEventListener.productTypeMap.get(tx.getProductId());
                String allowedDate = ProductEventListener.allowedMovementDateMap.get(tx.getProductId());

                if ("FIXED_TERM".equals(type)) {
                    LocalDate today = LocalDate.now();
                    LocalDate allowed = LocalDate.parse(allowedDate);
                    if (today.getDayOfMonth() != allowed.getDayOfMonth()) {
                        return Mono.error(new IllegalStateException("Cuentas a Plazo Fijo solo permiten movimientos el día "
                                + allowed.getDayOfMonth() + " de cada mes"));
                    }
                }

                tx.setDate(LocalDateTime.now());
                return repository.save(tx); 
            });
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
