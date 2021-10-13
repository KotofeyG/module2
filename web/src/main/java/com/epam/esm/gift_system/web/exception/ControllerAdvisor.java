package com.epam.esm.gift_system.web.exception;

import com.epam.esm.gift_system.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_CODE = "errorCode";
    private final ResourceBundleMessageSource messages;

    @Autowired
    public ControllerAdvisor(ResourceBundleMessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e, Locale locale) {
        return new ResponseEntity<>(createResponse(e.getErrorCode(), locale), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityAlreadyExistsException.class, EntityIsUsedException.class})
    public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException e, Locale locale) {
        return new ResponseEntity<>(createResponse(e.getErrorCode(), locale), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EntityNotValidNameException.class, EntityNotValidDescriptionException.class
            , EntityNotValidPriceException.class, EntityNotValidDurationException.class, EntityNotValidDateException.class
            , EntityNotValidTagNameException.class, EntityCreationException.class})
    public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityNotValidNameException e, Locale locale) {
        return new ResponseEntity<>(createResponse(e.getErrorCode(), locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Object> handleInternalServerException(InternalServerException e, Locale locale) {
        return new ResponseEntity<>(createResponse(e.getErrorCode(), locale), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createResponse(int errorCode, Locale locale) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ERROR_MESSAGE, messages.getMessage(getMessageByCode(errorCode), null, locale));
        response.put(ERROR_CODE, errorCode);
        return response;
    }

    private String getMessageByCode(int errorCode) {
        return "error_msg." + errorCode;
    }
}