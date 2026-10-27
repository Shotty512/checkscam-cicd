package com.example.checkscam.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class FileUploadValidationException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;
    private final HttpStatus httpStatus;

    public FileUploadValidationException(String messageKey, HttpStatus httpStatus, Object... args) {
        super(String.format("File upload validation failed for key %s with args %s", messageKey, Arrays.toString(args)));
        this.messageKey = messageKey;
        this.args = args;
        this.httpStatus = httpStatus;
    }
    public String getMessageKey() { return messageKey; }
    public Object[] getArgs() { return args; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}
