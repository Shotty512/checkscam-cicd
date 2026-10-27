package com.example.checkscam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    INVALID_REQUEST(400, "Yêu cầu không hợp lệ"),
    UNAUTHORIZED(401, "Không có quyền truy cập"),
    BAD_REQUEST(400, "Dữ liệu không hợp lệ"),
    NOT_FOUND(404, "Không tìm thấy tài nguyên"),
    INTERNAL_ERROR(500, "Lỗi hệ thống");

    private final int errorCode;

    private final String message;
}
