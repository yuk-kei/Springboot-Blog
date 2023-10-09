package com.yukkei;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
public class AbstractTestcontainers {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgresDBContainer.getJdbcUrl(),
                postgresDBContainer.getUsername(),
                postgresDBContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgresDBContainer =
            new PostgreSQLContainer<>("postgres:latest") // postgres image version
                    .withDatabaseName("yukkei-dao-unit-test")
                    .withUsername("yukkei")
                    .withPassword("password");


    @DynamicPropertySource
    private static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgresDBContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgresDBContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgresDBContainer::getPassword
        );
    }

    private static DataSource getDataSource(){
        DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName(postgresDBContainer.getDriverClassName())
                .url(postgresDBContainer.getJdbcUrl())
                .username(postgresDBContainer.getUsername())
                .password(postgresDBContainer.getPassword());
        return builder.build();
    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker FAKER = new Faker();

}
