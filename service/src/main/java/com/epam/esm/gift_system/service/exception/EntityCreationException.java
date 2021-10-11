package com.epam.esm.gift_system.service.exception;

public class EntityCreationException extends RuntimeException {
    private final int ERROR_CODE = 40007;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}