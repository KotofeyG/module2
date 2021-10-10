package com.epam.esm.gift_system.service.exception;

public class EntityNotValidPriceException extends RuntimeException {
    private static final int ERROR_CODE = 40003;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}