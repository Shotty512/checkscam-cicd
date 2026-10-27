package com.example.checkscam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScamInfoType {
    SDT(1, "Số điện thoại"),
    STK(2, "Số tài khoản"),
    URL(3, "Link website");

    private final Integer type;
    private final String value;

    public static ScamInfoType parse(Integer statusResult) {
        if (statusResult == null) {
            return null;
        }
        for (ScamInfoType e : ScamInfoType.values()) {
            if (e.type.equals(statusResult)) {
                return e;
            }
        }
        return null;
    }
}
