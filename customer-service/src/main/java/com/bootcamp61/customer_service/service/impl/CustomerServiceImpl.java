package com.bootcamp61.customer_service.service.impl;

import org.springframework.stereotype.Service;

import com.bootcamp61.customer_service.event.CustomerEventPublisher;
import com.bootcamp61.customer_service.model.Customer;
import com.bootcamp61.customer_service.repository.CustomerRepository;
import com.bootcamp61.customer_service.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    // @Autowired
    // private KafkaTemplate<String, String> kafkaTemplate;

    private final CustomerEventPublisher customerEventPublisher;

    private final CustomerRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Customer> create(Customer customer) {
        return repository.save(customer)
                         .doOnSuccess(saved -> customerEventPublisher.publishCustomerCreated(saved));
    }

    @Override
    public Mono<Customer> update(String id, Customer customer) {
        return repository.findById(id)
                         .flatMap(existing -> {
                            existing.setName(customer.getName());
                            existing.setDni(customer.getDni());
                            existing.setType(customer.getType());
                            existing.setHolders(customer.getHolders());
                            existing.setSigners(customer.getSigners());
                            return repository.save(existing);
                         });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
    
    // public void publishCustomerCreated(Customer customer){
    //     try{
    //         CustomerCreatedEvent event = CustomerCreatedEvent.builder()
    //                                                          .customerId(customer.getId())
    //                                                          .type(customer.getType())
    //                                                          .build();

    //         String json = objectMapper.writeValueAsString(event);
    //         kafkaTemplate.send("customer-events", json);
    //     } catch (Exception e){
    //         // log.error("Error sending kafka event", e);
    //     }
    // }

}
