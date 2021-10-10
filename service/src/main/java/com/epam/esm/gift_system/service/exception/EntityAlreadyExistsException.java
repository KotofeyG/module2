package com.epam.esm.gift_system.service.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    private final int ERROR_CODE = 40901;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}