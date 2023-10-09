package com.yukkei;

import com.github.javafaker.Faker;
import com.yukkei.customer.Customer;
import com.yukkei.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootExampleApplication.class, args);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            var name = faker.name();
            Random random = new Random();
            var firstName = name.firstName();
            var lastName = name.lastName();
            Customer customer = new Customer(
                    firstName,
                    lastName,
                    firstName.toLowerCase() + lastName.toLowerCase()  +  "@gmail.com",
                    random.nextInt(16,75)
            );

            customerRepository.save(customer);
        };
    }
}
