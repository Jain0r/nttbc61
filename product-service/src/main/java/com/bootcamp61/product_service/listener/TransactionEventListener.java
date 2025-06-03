package com.bootcamp61.product_service.listener;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bootcamp61.product_service.event.ProductTransactionUpdate;
import com.bootcamp61.product_service.model.BankAccount;
import com.bootcamp61.product_service.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionEventListener {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final ConcurrentHashMap<String, BankAccount> postTransactionHash = new ConcurrentHashMap<>();

    private final BankAccountService bankAccountService;

    @KafkaListener(topics = "transaction-event", groupId = "product-service-group")
    public void listen(String message){
        try{
            System.out.println("ENTRO A LISTENER");

            ProductTransactionUpdate info = objectMapper.readValue(message, ProductTransactionUpdate.class);
            BankAccount upd = BankAccount.builder()
                                         .id(info.getIdProduct())
                                         .balance(info.getNewBalance())
                                         .build();
            log.info("Se recibio transaccion: id-" + upd.getId() + " newbalance-" + upd.getBalance());

            // postTransactionHash.put(message, info)
            bankAccountService.update(upd.getId(), upd).subscribe();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
