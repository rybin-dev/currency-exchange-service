package com.rybindev.currencyexchangeservice.mapper;

public interface Mapper<F,T>{
    T mapFrom(F object);
}
