package com.example.bankingapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Owner name is required")
    private String owner;

    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 4, message = "PIN must be 4 digits long")
    @Pattern(regexp = "\\d{4}", message = "PIN must be numeric")
    private String pin;
}
