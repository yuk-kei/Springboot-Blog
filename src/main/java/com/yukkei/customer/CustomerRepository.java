package com.yukkei.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {
    // Integer is the identifier of the Customer entity
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer customerId);
}
