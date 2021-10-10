package com.epam.esm.gift_system.service.exception;

public class EntityNotValidTagNameException extends RuntimeException {
    private static final int ERROR_CODE = 40006;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}