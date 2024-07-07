package com.pagbank.challenge.infrastructure.api.controllers;

import com.pagbank.challenge.domain.exceptions.DomainException;
import com.pagbank.challenge.domain.exceptions.NotFoundException;
import com.pagbank.challenge.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final DomainException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(exception));
    }

    record ApiError(String message, List<Error> errors) {
        static ApiError from(final DomainException exception) {
            return new ApiError(exception.getMessage(), exception.getErrors());
        }
    }
}
