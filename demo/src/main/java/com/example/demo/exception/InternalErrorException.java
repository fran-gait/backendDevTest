package com.example.demo.exception;

import lombok.Getter;

@Getter
public class InternalErrorException extends RuntimeException {

    private final int statusCode;

    private static final String ERROR_FORMAT = "Error in the external service call: %s";

    public InternalErrorException(int statusCode, String message) {
        super(String.format(ERROR_FORMAT, message));
        this.statusCode = statusCode;
    }

}
