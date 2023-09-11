package com.example.spring.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PersonRecordDto(@NotBlank String name, @NotNull BigDecimal wage, @NotBlank String job) {

}
