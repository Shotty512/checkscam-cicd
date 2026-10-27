package com.example.checkscam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusReportEnum {
    PENDING(1, "Chờ xử lý"),
    APPROVED(2, "Đã xác nhận"),
    REJECTED(3, "Đã từ chối");

    private final Integer type;
    private final String value;

    public static StatusReportEnum parse(Integer statusResult) {
        if (statusResult == null) {
            return null;
        }
        for (StatusReportEnum e : StatusReportEnum.values()) {
            if (e.type.equals(statusResult)) {
                return e;
            }
        }
        return null;
    }
}
