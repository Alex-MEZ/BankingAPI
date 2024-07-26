package com.example.bankingapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    private BigDecimal amount;
    private String pin;
}
