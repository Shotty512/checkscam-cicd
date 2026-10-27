package com.example.checkscam.exception;

public class InvalidCaptchaException extends RuntimeException {
    public InvalidCaptchaException() {
        super("CAPTCHA validation failed");
    }
}
