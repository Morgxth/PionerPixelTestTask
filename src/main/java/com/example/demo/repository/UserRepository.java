package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    boolean existsByName(String name);

    @Cacheable(value = "usersByEmail", key = "#email")
    Optional<User> findByEmails_Email(String email);
    @Cacheable(value = "usersByPhone", key = "#phone")
    Optional<User> findByPhones_Phone(String phone);

    List<User> findByNameStartingWith(String name, Pageable pageable);
    List<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);
}

