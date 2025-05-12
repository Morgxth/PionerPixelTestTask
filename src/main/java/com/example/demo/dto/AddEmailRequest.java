package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddEmailRequest {
    @NotBlank
    @Email
    private String email;
}
