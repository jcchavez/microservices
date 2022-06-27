package com.ioSoftware.customerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioSoftware.customerservice.repository.entity.Customer;
import com.ioSoftware.customerservice.repository.entity.Region;
import com.ioSoftware.customerservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class CustomerRest {

    @Autowired
    private CustomerService customerService;

    // retrieve all customers
    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId",required = false) Long regionId){
        List<Customer> customers = new ArrayList<>();
        if(regionId == null){//list all
            customers = customerService.findAllCustomers();
            if(customers.isEmpty()){
                return ResponseEntity.noContent().build();//204 http status no content
            }
        }else {
            customers = customerService.findCustomersByRegion(Region.builder().id(regionId).build());
            if(customers.isEmpty()){
                log.error("Customers with Region id {} not found.",regionId);
                return ResponseEntity.notFound().build();// http status no found
            }
        }

        return ResponseEntity.ok(customers);

    }
    
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        log.info("Creating customer : {} ", customer);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        
        
        Customer customerBD = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerBD);
        
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer){
        Customer customerBD = customerService.getCustomer(id);
        if(customerBD == null){
            return ResponseEntity.notFound().build();// http status no found
        }
        customer.setId(id);
        customer.setState("Updated");
         customerBD = customerService.updateCustomer(customer);
         return ResponseEntity.ok(customerBD);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id){
        Customer customerBD = customerService.getCustomer(id);
        if(customerBD == null){
            return ResponseEntity.notFound().build();// http status no found
        }
        customerBD = customerService.deleteCustomer(customerBD);

        return ResponseEntity.ok(customerBD);
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();
        //Format the messages to Json
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException jsonProcessingException){
            jsonProcessingException.printStackTrace();
        }
        return jsonString;
    }

}
