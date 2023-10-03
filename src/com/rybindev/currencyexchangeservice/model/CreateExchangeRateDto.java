package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public final class CreateExchangeRateDto {
    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final BigDecimal rate;
}
