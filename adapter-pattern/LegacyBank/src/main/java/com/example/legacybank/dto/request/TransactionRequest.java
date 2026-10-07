package com.example.legacybank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank(message = "accountRef is required") String accountRef,
        @NotNull @Positive(message = "amount must be greater than zero") BigDecimal amount,
        @NotBlank(message = "currencyCode is required")
        @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter code like USD")
        String currencyCode) {
}
