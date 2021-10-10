package com.epam.esm.gift_system.service.exception;

public class EntityNotValidDescriptionException extends RuntimeException {
    private static final int ERROR_CODE = 40002;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}