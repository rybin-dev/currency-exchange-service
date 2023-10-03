package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeResponse {
    private final CurrencyDto baseCurrency;
    private final CurrencyDto targetCurrency;
    private final BigDecimal rate;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;

}
