package com.yukkei.customer;

import com.yukkei.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                FAKER.number().numberBetween(18, 100)
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(c.getLastName()).isEqualTo(customer.getLastName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1; // Be careful, this id must not exist in the database

        //When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );
        underTest.insertCustomer(customer);

        //When
        boolean actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExist() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        boolean actual = underTest.existsPersonWithEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsPersonWithId(id);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnsFalseWhenIdDoesNotExist() {
        //Given
        int id = -1; // Be careful, this id must not exist in the database

        //When
        var actual = underTest.existsPersonWithId(id);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newFirstName = FAKER.name().firstName();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setFirstName(newFirstName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(newFirstName); // changed
            assertThat(c.getLastName()).isEqualTo(customer.getLastName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerLastName(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newLastName = FAKER.name().lastName();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setLastName(newLastName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(c.getLastName()).isEqualTo(newLastName); // changed
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerEmail(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(c.getLastName()).isEqualTo(customer.getLastName());
            assertThat(c.getEmail()).isEqualTo(newEmail); // changed
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerAge(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = FAKER.number().numberBetween(18, 100);

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(c.getLastName()).isEqualTo(customer.getLastName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge); // changed
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer(){
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When update with new first name, last name, email and age
        Customer update = new Customer();
        update.setId(id);
        update.setFirstName(FAKER.name().firstName());
        update.setLastName(FAKER.name().lastName());
        update.setEmail(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID());
        update.setAge(FAKER.number().numberBetween(18, 100));

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate(){
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                email,
                FAKER.number().numberBetween(18, 100)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getFirstName()).isEqualTo(customer.getFirstName());
            assertThat(c.getLastName()).isEqualTo(customer.getLastName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }
}
