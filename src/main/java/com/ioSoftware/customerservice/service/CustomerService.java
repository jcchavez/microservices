package com.ioSoftware.customerservice.service;

import com.ioSoftware.customerservice.repository.entity.Customer;
import com.ioSoftware.customerservice.repository.entity.Region;

import java.util.List;

public interface CustomerService {
    List<Customer> findAllCustomers();

    Customer getCustomer(Long id);

    List<Customer> findCustomersByRegion(Region region);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    Customer deleteCustomer(Customer customer);
}
