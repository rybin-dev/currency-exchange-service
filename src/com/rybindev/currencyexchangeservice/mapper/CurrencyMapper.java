package com.rybindev.currencyexchangeservice.mapper;

import com.rybindev.currencyexchangeservice.model.Currency;
import com.rybindev.currencyexchangeservice.model.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyMapper implements Mapper<Currency, CurrencyDto> {
    private static final CurrencyMapper INSTANCE = new CurrencyMapper();

    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public CurrencyDto mapFrom(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .sign(currency.getSign())
                .build();
    }
}
