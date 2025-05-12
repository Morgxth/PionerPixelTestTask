package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    List<UserDto> searchUsers(String name, String email, String phone, LocalDate dob, int page, int size);

    void addEmail(Long userId, String email);
    void removeEmail(Long userId, String email);

    void addPhone(Long userId, String phone);
    void removePhone(Long userId, String phone);
}

