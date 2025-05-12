package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransferRequest {
    @NotNull
    private Long toUserId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Минимальная сумма перевода — 0.01")
    private BigDecimal amount;
}