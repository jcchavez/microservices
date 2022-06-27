package com.ioSoftware.customerservice.service;

import com.ioSoftware.customerservice.repository.CustomerRepository;
import com.ioSoftware.customerservice.repository.entity.Customer;
import com.ioSoftware.customerservice.repository.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAllCustomers(){
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id){
        return customerRepository.findById(id).orElse(null);
    }


    @Override
    public List<Customer> findCustomersByRegion(Region region){
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer){
        //to make the POST method idempotent validating with another numberId unique for each customer
        Customer customerBD = customerRepository.findByNumberId(customer.getNumberId());
        if(customerBD != null){
            return customerBD;
        }
        customer.setState("Created");
        customerBD = customerRepository.save(customer);
        return customerBD;
    }

    @Override
    public Customer updateCustomer(Customer customer){
        Customer customerBD = getCustomer(customer.getId());
        if(customerBD == null){
            return null;
        }
        customerBD.setFirstName(customer.getFirstName());
        customerBD.setLastName(customer.getLastName());
        customerBD.setEmail(customer.getEmail());
        customerBD.setPhotoUrl(customer.getPhotoUrl());

        return customerRepository.save(customerBD);
    }

    @Override
    public Customer deleteCustomer(Customer customer){
        Customer customerBD = getCustomer(customer.getId());
        if(customerBD == null){
            return null;
        }
        customerBD.setState("Deleted");
        return customerRepository.save(customerBD);
    }
}
