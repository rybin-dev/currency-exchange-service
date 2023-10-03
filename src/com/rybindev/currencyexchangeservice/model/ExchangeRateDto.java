package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ExchangeRateDto {
    private Integer id;
    private CurrencyDto base;
    private CurrencyDto target;
    private BigDecimal rate;
}
