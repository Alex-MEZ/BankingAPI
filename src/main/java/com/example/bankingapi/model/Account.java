package com.example.bankingapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    @Column(nullable = false, length = 4)
    private String pin;

    private BigDecimal balance = BigDecimal.ZERO;

    public void setPin(String pin) {
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN должен быть из 4 цифр!");
        }
        this.pin = pin;
    }
}

