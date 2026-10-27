package com.example.checkscam.repository.projection;

import java.time.LocalDateTime;

public interface BankScamStatsInfo {
    Long getId();

    String bankAccount();

    Integer getVerifiedCount();

    String getReasonsJson();

    LocalDateTime getLastReportAt();
}