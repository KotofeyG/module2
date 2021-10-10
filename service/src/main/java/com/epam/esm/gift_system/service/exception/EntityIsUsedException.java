package com.epam.esm.gift_system.service.exception;

public class EntityIsUsedException extends RuntimeException {
    private final int ERROR_CODE = 40902;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}