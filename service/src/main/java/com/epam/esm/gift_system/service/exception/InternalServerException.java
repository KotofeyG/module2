package com.epam.esm.gift_system.service.exception;

public class InternalServerException extends RuntimeException {
    private static final int ERROR_CODE = 50001;

    public int getErrorCode() {
        return ERROR_CODE;
    }
}