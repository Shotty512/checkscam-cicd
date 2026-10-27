package com.example.checkscam.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BankScamStatsInfoDto {
    private Long id;
    private String bankAccount;
    private LocalDateTime lastReportAt;
    private Integer verifiedCount;

    public BankScamStatsInfoDto(Long id, String bankAccount, LocalDateTime lastReportAt, Integer verifiedCount) {
        this.id = id;
        this.bankAccount = bankAccount;
        this.lastReportAt = lastReportAt;
        this.verifiedCount = verifiedCount;
    }
}
