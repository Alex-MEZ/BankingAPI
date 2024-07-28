package com.example.bankingapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateAccountRequest {
    @NotNull
    private String owner;

    @Pattern(regexp = "\\d{4}", message = "PIN должен быть из 4 цифр")
    private String pin;
}
