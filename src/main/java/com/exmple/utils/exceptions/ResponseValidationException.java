package com.exmple.utils.exceptions;

public class ResponseValidationException extends Exception {
    public ResponseValidationException(String errorMessage) {
        super(errorMessage);
    }
}
