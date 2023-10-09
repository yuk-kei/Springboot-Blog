package com.yukkei.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    List<Customer> customers = List.of(
            new Customer(1, "Mario", "Rossi", "marionro@gmail,com", 30),
            new Customer(2, "Luigi", "Verdi", "luigiver@gmail,com", 40),
            new Customer(3, "Giovanni", "Bianchi", "giovannibi@gmail.com", 50)
    );
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return  customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Integer customerId) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(customerId));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .ifPresent(customers::remove);
        //Same as .ifPresent(c -> customers.remove(c));
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}
