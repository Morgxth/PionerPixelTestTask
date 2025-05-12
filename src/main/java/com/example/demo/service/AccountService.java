package com.example.demo.service;

import com.example.demo.entity.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findByUserId(Long userId);

    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);

    void increaseBalancePeriodically();

    void initAccount(Long userId, BigDecimal initialBalance);
}
