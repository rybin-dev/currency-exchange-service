package com.rybindev.currencyexchangeservice.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeRequest {
    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final BigDecimal amount;
}
