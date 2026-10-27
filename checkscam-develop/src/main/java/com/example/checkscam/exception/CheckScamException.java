package com.example.checkscam.exception;


import lombok.Getter;
import com.example.checkscam.constant.ErrorCodeEnum;

@Getter
public class CheckScamException extends Exception {
    private final int code;
    private final String message;

    public CheckScamException(ErrorCodeEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.message = errorEnum.getMessage();
    }

    public CheckScamException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
