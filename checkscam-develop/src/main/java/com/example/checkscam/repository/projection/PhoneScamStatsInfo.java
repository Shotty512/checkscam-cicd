package com.example.checkscam.repository.projection;

import java.time.LocalDateTime;

public interface PhoneScamStatsInfo {
    Long getId();

    String getPhoneNumber();

    Integer getVerifiedCount();

    String getReasonsJson();

    LocalDateTime getLastReportAt();
}