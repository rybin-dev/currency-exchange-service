package com.rybindev.currencyexchangeservice.mapper;

import com.rybindev.currencyexchangeservice.model.Currency;
import com.rybindev.currencyexchangeservice.model.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDtoMapper implements Mapper<CurrencyDto, Currency>{
    private static final CurrencyDtoMapper INSTANCE = new CurrencyDtoMapper();

    public static CurrencyDtoMapper getInstance(){
        return INSTANCE;
    }

    @Override
    public Currency mapFrom(CurrencyDto dto) {
        return Currency.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .sign(dto.getSign())
                .build();
    }
}
