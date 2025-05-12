package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddPhoneRequest {
    @NotBlank
    @Pattern(regexp = "7\\d{10}", message = "Формат телефона должен быть 7920XXXXXXX")
    private String phone;
}
