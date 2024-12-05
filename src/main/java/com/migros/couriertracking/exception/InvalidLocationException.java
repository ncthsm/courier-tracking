package com.migros.couriertracking.exception;

import org.springframework.http.HttpStatus;

public class InvalidLocationException extends BaseException {
    public InvalidLocationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
} 