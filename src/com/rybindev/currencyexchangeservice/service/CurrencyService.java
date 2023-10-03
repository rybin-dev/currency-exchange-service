package com.rybindev.currencyexchangeservice.service;

import com.rybindev.currencyexchangeservice.dao.CurrencyDao;
import com.rybindev.currencyexchangeservice.exception.NotFoundException;
import com.rybindev.currencyexchangeservice.mapper.CurrencyDtoMapper;
import com.rybindev.currencyexchangeservice.mapper.CurrencyMapper;
import com.rybindev.currencyexchangeservice.model.Currency;
import com.rybindev.currencyexchangeservice.model.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CurrencyMapper currencyMapper = CurrencyMapper.getInstance();
    private final CurrencyDtoMapper currencyDtoMapper = CurrencyDtoMapper.getInstance();

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll()
                .stream()
                .map(currencyMapper::mapFrom)
                .toList();
    }

    public CurrencyDto findByCode(String code) {
        return currencyDao.findByCode(code)
                .map(currencyMapper::mapFrom)
                .orElseThrow(NotFoundException::new);

    }

    public CurrencyDto findById(Integer id) {
        return currencyDao.findById(id)
                .map(currencyMapper::mapFrom)
                .orElseThrow(NotFoundException::new);

    }

    public CurrencyDto update(CurrencyDto currencyDto) {
        currencyDao.update(currencyDtoMapper.mapFrom(currencyDto));
        return currencyDto;
    }

    public CurrencyDto save(CurrencyDto currencyDto) {
        Currency save = currencyDao.save(currencyDtoMapper.mapFrom(currencyDto));
        return currencyMapper.mapFrom(save);
    }

    public boolean delete(Integer id) {
        return currencyDao.delete(id);
    }
}
