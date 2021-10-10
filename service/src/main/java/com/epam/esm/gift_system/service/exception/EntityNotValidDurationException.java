package com.epam.esm.gift_system.service.exception;

public class EntityNotValidDurationException extends RuntimeException {
    private static final int ERROR_CODE = 40004;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}