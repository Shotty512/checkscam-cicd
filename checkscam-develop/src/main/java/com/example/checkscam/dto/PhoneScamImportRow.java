package com.example.checkscam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneScamImportRow {
    private String phoneNumber;
    private String description;
    private Integer verifiedCount;
    private String lastReportAt;
}
