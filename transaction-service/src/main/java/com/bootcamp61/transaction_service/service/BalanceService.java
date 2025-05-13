package com.bootcamp61.transaction_service.service;

import java.math.BigDecimal;

import reactor.core.publisher.Mono;


public interface BalanceService {
    
    Mono<BigDecimal> getBalanceForProduct(String productId);


}
