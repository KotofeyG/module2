package com.epam.esm.gift_system.service.exception;

public class EntityNotValidNameException extends RuntimeException {
    private final int ERROR_CODE = 40001;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}