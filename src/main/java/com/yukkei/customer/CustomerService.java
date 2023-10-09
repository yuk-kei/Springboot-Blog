package com.yukkei.customer;

import com.yukkei.exception.DuplicateResourceException;
import com.yukkei.exception.RequestValidationException;
import com.yukkei.exception.ResourceNotFindException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }
    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFindException("Customer " + id + " not found"));
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDao.existsPersonWithId(customerId)) {
            throw new ResourceNotFindException("Customer " + customerId + " not found");
        }

        customerDao.deleteCustomerById(customerId);
    }

    public void addNewCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException(
                    "Email " + customerRegistrationRequest.email() + " already taken");
        }
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.firstName(),
                customerRegistrationRequest.lastName(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
            ));

    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomerById(customerId);

        boolean changed = false;

        if (updateRequest.firstName() != null &&
                !updateRequest.firstName().equals(customer.getFirstName())) {
            customer.setFirstName(updateRequest.firstName());
            changed = true;
        }

        if (updateRequest.lastName() != null &&
                !updateRequest.lastName().equals(customer.getLastName())) {
            customer.setLastName(updateRequest.lastName());
            changed = true;
        }

        if (updateRequest.email() != null &&
                !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email " + updateRequest.email() + " already taken");
            }
            customer.setEmail(updateRequest.email());
            changed = true;
        }

        if (updateRequest.age() != null &&
                !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("no data change found");
        }

        customerDao.updateCustomer(customer);
    }
}
