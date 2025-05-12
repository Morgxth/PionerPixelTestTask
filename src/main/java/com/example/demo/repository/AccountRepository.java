package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Cacheable(value = "accounts", key = "#userId")
    Optional<Account> findByUserId(Long userId);
}