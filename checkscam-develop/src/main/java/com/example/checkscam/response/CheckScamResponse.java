package com.example.checkscam.response;

import com.example.checkscam.constant.ErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckScamResponse<T> {
    private int code;
    private String message;
    private T data;
    private long total;

    public CheckScamResponse(T data) {
        this.data = data;
        this.code = 200;
    }

    public CheckScamResponse(int code, String message, T data, long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public CheckScamResponse(ErrorCodeEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.message = errorEnum.getMessage();
    }

    public CheckScamResponse(T data, ErrorCodeEnum errorEnum) {
        this.data = data;
        this.code = errorEnum.getErrorCode();
        this.message = errorEnum.getMessage();
    }

    public CheckScamResponse(T data, long total) {
        this.data = data;
        this.code = 200;
        this.total = total;
    }

    public CheckScamResponse(int code, T data, long total) {
        this.code = code;
        this.data = data;
        this.total = total;
    }
    public CheckScamResponse(String message) {
        this.message = message;
    }

    public CheckScamResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
