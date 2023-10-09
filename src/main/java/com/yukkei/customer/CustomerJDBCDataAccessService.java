package com.yukkei.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, first_name, last_name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, first_name, last_name, email, age
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(first_name, last_name, email, age)
                VALUES(?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAge()
        );

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, customerId);
        System.out.println("deleteCustomerByIde result  = " + result);
    }

    @Override
    public void updateCustomer(Customer update) {
        if(update.getFirstName() != null) {
            var sql = " UPDATE customer SET first_name = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getFirstName(),
                    update.getId()
            );
            System.out.println("update.getFirstName() result = " + result);
        }

        if(update.getLastName() != null) {
            var sql = " UPDATE customer SET last_name = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getLastName(),
                    update.getId()
            );
            System.out.println("update.getLastName() result = " + result);
        }

        if(update.getEmail() != null) {
            var sql = "UPDATE customer SET email = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            System.out.println("update.getEmail() result = " + result);
        }

        if(update.getAge() != null) {
            var sql = "UPDATE customer SET age = ? WHERE id = ? ";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println("update.getAge() result = " + result);
        }
    }

}
