package com.migros.couriertracking.exception;

import org.springframework.http.HttpStatus;

public class DuplicateVisitException extends BaseException {
    public DuplicateVisitException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
} 