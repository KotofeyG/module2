package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {
    private final int ERROR_CODE = 40401;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}