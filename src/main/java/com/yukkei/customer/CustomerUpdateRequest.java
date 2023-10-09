package com.yukkei.customer;

public record CustomerUpdateRequest (
        String firstName,
        String lastName,
        String email,
        Integer age
){
}
