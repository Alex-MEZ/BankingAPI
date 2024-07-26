package com.example.bankingapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private String owner;
    private String pin;
}
