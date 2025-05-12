
package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.EmailData;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class AccountTransferIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setupProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Test
    void testTransferBetweenUsers() {
        User user = new User(1L, "Ivan Testov", LocalDate.now(), "$2a$10$6kmXQ7c4u1GzBnMGL9sIwe8c0IacMKxUyEgtTDRyMT9bjs0uEtXWq", new Account(), new ArrayList<>(), new ArrayList<>());
        User user1 = new User(2L, "Ivan Zestov", LocalDate.now(), "$2a$10$6kmXQ7c4u1GzBnMGL9sIwe8c0IacMKxUyEgtTDRyMT9bjs0uEtXWq", new Account(), new ArrayList<>(), new ArrayList<>());
        EmailData emailData = new EmailData(user.getId(), "test@example.com", user);
        EmailData emailData1 = new EmailData(user1.getId(), "test1@example.com", user1);
        user.setEmails(List.of(emailData));
        user1.setEmails(List.of(emailData1));
        Account account = new Account(user.getId(), user, new BigDecimal(500), new BigDecimal(500));
        Account account2 = new Account(user1.getId(), user1, new BigDecimal(500), new BigDecimal(500));
        user.setAccount(account);
        user1.setAccount(account2);
        userRepository.save(user);
        userRepository.save(user1);
        // Инициализация аккаунтов
//        accountService.initAccount(user.getId(), BigDecimal.valueOf(1000));
//        accountService.initAccount(user1.getId(), BigDecimal.valueOf(200));

        // Перевод средств
        accountService.transfer(user.getId(), user1.getId(), BigDecimal.valueOf(200));

        // Проверка новых балансов
        Account updatedTestov = accountService.findByUserId(user.getId()).orElseThrow();
        Account updatedZestov = accountService.findByUserId(user1.getId()).orElseThrow();

        assertEquals(BigInteger.valueOf(300), updatedTestov.getBalance().toBigInteger());
        assertEquals(BigInteger.valueOf(700), updatedZestov.getBalance().toBigInteger());
    }
}


