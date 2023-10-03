package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class CurrencyDto {
    private final Integer id;
    private final String code;
    private final String name;
    private final String sign;
}
