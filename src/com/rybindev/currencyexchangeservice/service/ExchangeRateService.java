package com.rybindev.currencyexchangeservice.service;

import com.rybindev.currencyexchangeservice.dao.CurrencyDao;
import com.rybindev.currencyexchangeservice.dao.ExchangeRateDao;
import com.rybindev.currencyexchangeservice.exception.BadRequestException;
import com.rybindev.currencyexchangeservice.exception.NotFoundException;
import com.rybindev.currencyexchangeservice.mapper.ExchangeRateMapper;
import com.rybindev.currencyexchangeservice.model.CreateExchangeRateDto;
import com.rybindev.currencyexchangeservice.model.Currency;
import com.rybindev.currencyexchangeservice.model.ExchangeRate;
import com.rybindev.currencyexchangeservice.model.ExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public List<ExchangeRateDto> findAll() {
        return exchangeRateDao.findAll()
                .stream()
                .map(exchangeRateMapper::mapFrom)
                .toList();
    }

    public ExchangeRateDto findByBaseCodeAndTargetCode(String baseCode, String targetCode) {
        return exchangeRateDao.findByBaseCodeAndTargetCode(baseCode, targetCode)
                .map(exchangeRateMapper::mapFrom)
                .orElseThrow(NotFoundException::new);
    }

    public ExchangeRateDto findById(Integer id) {
        return exchangeRateDao.findById(id)
                .map(exchangeRateMapper::mapFrom)
                .orElseThrow(NotFoundException::new);
    }

    public ExchangeRateDto update(CreateExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = exchangeRateDao.findByBaseCodeAndTargetCode(
                        exchangeRateDto.getBaseCurrencyCode(),
                        exchangeRateDto.getTargetCurrencyCode())
                .orElseThrow(BadRequestException::new);
        exchangeRate.setRate(exchangeRateDto.getRate());

        ExchangeRate update = exchangeRateDao.update(exchangeRate);
        return exchangeRateMapper.mapFrom(update);
    }

    public ExchangeRateDto save(CreateExchangeRateDto exchangeRate) {
        Currency base = currencyDao.findByCode(exchangeRate.getBaseCurrencyCode())
                .orElseThrow(NotFoundException::new);
        Currency target = currencyDao.findByCode(exchangeRate.getTargetCurrencyCode())
                .orElseThrow(NotFoundException::new);

        ExchangeRate exchangeRateNew = ExchangeRate.builder()
                .base(base)
                .target(target)
                .rate(exchangeRate.getRate())
                .build();

        exchangeRateDao.save(exchangeRateNew);
        return exchangeRateMapper.mapFrom(exchangeRateNew);
    }

    public boolean delete(Integer id) {
        return exchangeRateDao.delete(id);
    }

}
