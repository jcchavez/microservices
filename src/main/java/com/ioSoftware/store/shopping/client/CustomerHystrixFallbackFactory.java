package com.ioSoftware.store.shopping.client;

import com.ioSoftware.store.shopping.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component //enable to allow dependency injection
public class CustomerHystrixFallbackFactory  implements CustomerClient{
    @Override
    public ResponseEntity<Customer> getCustomer(long id) { // Plan B if our Customer Service Fails
        Customer customer = Customer.builder()
                .firstName("none")
                .lastName("none")
                .email("none")
                .photoUrl("none").build();
        return ResponseEntity.ok(customer);
    }
}
