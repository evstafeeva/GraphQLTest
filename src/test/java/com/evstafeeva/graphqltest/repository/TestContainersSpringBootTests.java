package com.evstafeeva.graphqltest.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public abstract class TestContainersSpringBootTests {

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:14.4")
        .withDatabaseName("public")
        .withPassword("postgres")
        .withUsername("postgres");

    @DynamicPropertySource
    static void dataBaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.flyway.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.flyway.password", postgresqlContainer::getPassword);
        registry.add("spring.flyway.user", postgresqlContainer::getUsername);
        registry.add("spring.flyway.enabled", () -> Boolean.TRUE);
    }
}