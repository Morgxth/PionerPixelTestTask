package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Account> findByUserId(Long userId) {
        Account userAccount = accountRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("Аккаунт пользователя не найден"));
        return Optional.of(userAccount);
    }

    @Override
    public void initAccount(Long userId, BigDecimal initialBalance){
        if (accountRepository.findByUserId(userId).isPresent()){
            throw new IllegalStateException("Аккаунт уже существует");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        Account account = new Account();
        account.setUser(user);
        account.setBalance(initialBalance);
        accountRepository.save(account);
    }

    @Override
    @Scheduled(fixedRate = 30000)
    public void increaseBalancePeriodically() {
        List<Account> accounts = accountRepository.findAll();

        for (Account acc : accounts) {
            BigDecimal current = acc.getBalance();
            BigDecimal initial = acc.getInitialBalance();
            BigDecimal max = initial.multiply(BigDecimal.valueOf(2.07));

            BigDecimal increased = current.multiply(BigDecimal.valueOf(1.10));
            if (increased.compareTo(max) > 0) {
                increased = max;
            }

            acc.setBalance(increased);
        }

        accountRepository.saveAll(accounts);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "accounts", key = "#fromUserId"),
            @CacheEvict(value = "accounts", key = "#toUserId")
    })
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        synchronized (this) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Сумма перевода должна быть положительной");
            }
            if (fromUserId.equals(toUserId)) {
                throw new IllegalArgumentException("Аккаунты отправителя и получателя не могут быть одинаковыми");
            }
            Account from = accountRepository.findByUserId(fromUserId)
                    .orElseThrow(() -> new NoSuchElementException("Аккаунт отправителя не найден"));

            Account to = accountRepository.findByUserId(toUserId)
                    .orElseThrow(() -> new NoSuchElementException("Аккаунт получателя не найден"));

            if (from.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Недостаточно средств");
            }

            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));

            accountRepository.save(from);
            accountRepository.save(to);
        }
    }
}
