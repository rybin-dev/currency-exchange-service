package com.rybindev.currencyexchangeservice.service;

import com.rybindev.currencyexchangeservice.dao.ExchangeRateDao;
import com.rybindev.currencyexchangeservice.exception.BadRequestException;
import com.rybindev.currencyexchangeservice.mapper.CurrencyMapper;
import com.rybindev.currencyexchangeservice.model.ExchangeRate;
import com.rybindev.currencyexchangeservice.model.ExchangeRequest;
import com.rybindev.currencyexchangeservice.model.ExchangeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeService {
    private static final String CURRENCY_CODE = "USD";
    private static final MathContext MATH_CONTEXT = new MathContext(5, RoundingMode.HALF_EVEN);

    private static final ExchangeService INSTANCE = new ExchangeService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrencyMapper currencyMapper = CurrencyMapper.getInstance();

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    public ExchangeResponse exchange(ExchangeRequest request) {
        ExchangeRate exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode(
                request.getBaseCurrencyCode(),
                request.getTargetCurrencyCode())
                .orElse(null);

        if (exchangeRate != null) {
            BigDecimal convertedAmount = request.getAmount().multiply(exchangeRate.getRate(), MATH_CONTEXT);

            return ExchangeResponse.builder()
                    .baseCurrency(currencyMapper.mapFrom(exchangeRate.getBase()))
                    .targetCurrency(currencyMapper.mapFrom(exchangeRate.getTarget()))
                    .rate(exchangeRate.getRate())
                    .amount(request.getAmount())
                    .convertedAmount(convertedAmount)
                    .build();
        }


        exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode(
                request.getTargetCurrencyCode(),
                request.getBaseCurrencyCode())
                .orElse(null);

        if (exchangeRate != null) {
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), MATH_CONTEXT);
            BigDecimal convertedAmount = request.getAmount().multiply(rate, MATH_CONTEXT);

            return ExchangeResponse.builder()
                    .baseCurrency(currencyMapper.mapFrom(exchangeRate.getTarget()))
                    .targetCurrency(currencyMapper.mapFrom(exchangeRate.getBase()))
                    .rate(exchangeRate.getRate())
                    .amount(request.getAmount())
                    .convertedAmount(convertedAmount)
                    .build();
        }


        ExchangeRate exchangeRate1 = exchangeRateDao.findByBaseCodeAndTargetCode(
                CURRENCY_CODE,
                request.getBaseCurrencyCode())
                .orElse(null);
        ExchangeRate exchangeRate2 = exchangeRateDao.findByBaseCodeAndTargetCode(
                CURRENCY_CODE,
                request.getTargetCurrencyCode())
                .orElse(null);

        if (exchangeRate1 == null || exchangeRate2 == null) {
            throw new BadRequestException();
        }

        BigDecimal rate = exchangeRate2.getRate()
                .divide(exchangeRate1.getRate(), MATH_CONTEXT);
        BigDecimal convertedAmount = request.getAmount()
                .multiply(request.getAmount(), MATH_CONTEXT);

        return ExchangeResponse.builder()
                .baseCurrency(currencyMapper.mapFrom(exchangeRate1.getTarget()))
                .targetCurrency(currencyMapper.mapFrom(exchangeRate2.getTarget()))
                .rate(rate)
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .build();
    }
}
