package com.example.demo;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccountServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransferServiceIntegrationTest {

    @Autowired
    private AccountServiceImpl transferService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private User fromUser;
    private User toUser;
    private Account fromAccount;
    private Account toAccount;

    @Before
    public void setUp() {
        // Создание пользователей
        fromUser = new User(null, "John Doe", LocalDate.of(1990, 1, 1), "password123", null, new ArrayList<>(), new ArrayList<>());
        toUser = new User(null, "Jane Doe", LocalDate.of(1992, 2, 2), "password456", null, new ArrayList<>(), new ArrayList<>());

        userRepository.save(fromUser);
        userRepository.save(toUser);

        // Создание аккаунтов с пользователями
        fromAccount = new Account(null, fromUser, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        toAccount = new Account(null, toUser, BigDecimal.valueOf(500), BigDecimal.valueOf(500));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @org.junit.Test
    public void testTransfer() {
        BigDecimal amount = BigDecimal.valueOf(100);

        // Проверяем исходный баланс
        assertEquals(BigDecimal.valueOf(1000), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(500), toAccount.getBalance());

        // Выполняем перевод
        transferService.transfer(fromUser.getId(), toUser.getId(), amount);

        // Проверяем обновленные балансы
        fromAccount = accountRepository.findById(fromAccount.getId()).get();
        toAccount = accountRepository.findById(toAccount.getId()).get();

        assertEquals(BigDecimal.valueOf(900), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(600), toAccount.getBalance());
    }

    @After
    public void tearDown() {
        accountRepository.delete(fromAccount);
        accountRepository.delete(toAccount);
        userRepository.delete(fromUser);
        userRepository.delete(toUser);
    }
}


