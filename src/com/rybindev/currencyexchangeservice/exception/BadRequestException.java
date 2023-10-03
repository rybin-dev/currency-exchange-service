package com.rybindev.currencyexchangeservice.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(Throwable e) {
        super(e);
    }
}
