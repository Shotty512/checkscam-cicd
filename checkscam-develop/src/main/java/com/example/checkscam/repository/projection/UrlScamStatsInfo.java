package com.example.checkscam.repository.projection;

import java.time.LocalDateTime;

public interface UrlScamStatsInfo {
    Long getId();

    String getUrl();

    Integer getVerifiedCount();

    String getReasonsJson();

    LocalDateTime getLastReportAt();
}