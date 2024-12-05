package com.migros.couriertracking.exception;

import org.springframework.http.HttpStatus;

public class DuplicateIdentityNumberException extends BaseException {
    public DuplicateIdentityNumberException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
} 