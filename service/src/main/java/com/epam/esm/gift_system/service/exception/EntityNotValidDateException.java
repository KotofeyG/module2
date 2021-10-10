package com.epam.esm.gift_system.service.exception;

public class EntityNotValidDateException extends RuntimeException {
    private static final int ERROR_CODE = 40005;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}