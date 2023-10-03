package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeRate {
    private Integer id;
    private Currency base;
    private Currency target;
    private BigDecimal rate;
}
