package com.yukkei.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Integer age
) {
}
