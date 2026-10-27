package com.example.checkscam.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhoneScamStatsInfoDto {
    private Long id;
    private String phoneNumber;
    private LocalDateTime lastReportAt;
    private Integer verifiedCount;
    private ReasonsJsonDto reasonsJson;

    public PhoneScamStatsInfoDto(Long id, String phoneNumber, LocalDateTime lastReportAt, Integer verifiedCount, ReasonsJsonDto reasonsJson) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.lastReportAt = lastReportAt;
        this.verifiedCount = verifiedCount;
        this.reasonsJson = reasonsJson;
    }
}
