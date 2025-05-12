package com.example.demo.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SearchUserRequest {
    private String name;
    private String email;
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private int page = 0;
    private int size = 10;
}
