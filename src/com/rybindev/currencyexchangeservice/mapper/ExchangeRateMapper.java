package com.rybindev.currencyexchangeservice.mapper;

import com.rybindev.currencyexchangeservice.model.ExchangeRate;
import com.rybindev.currencyexchangeservice.model.ExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateMapper implements Mapper<ExchangeRate, ExchangeRateDto> {
    private static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();

    private final CurrencyMapper currencyMapper = CurrencyMapper.getInstance();


    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ExchangeRateDto mapFrom(ExchangeRate object) {
        return ExchangeRateDto.builder()
                .id(object.getId())
                .base(currencyMapper.mapFrom(object.getBase()))
                .target(currencyMapper.mapFrom(object.getTarget()))
                .rate(object.getRate())
                .build();
    }
}
