package com.example.checkscam.dto;

import com.example.checkscam.entity.BankScamStats;
import com.example.checkscam.entity.PhoneScamStats;
import com.example.checkscam.entity.UrlScamStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ScamStatsDto {
    private  Long id;
    private  String info;
    private  Integer verifiedCount;
    private  ReasonsJsonDto reasonsJson;
    private  LocalDateTime lastReportAt;

    public ScamStatsDto (PhoneScamStats stats) {
        this.id = stats.getId();
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }

    public ScamStatsDto (UrlScamStats stats) {
        this.id = stats.getId();
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }

    public ScamStatsDto(BankScamStats stats) {
        this.id = stats.getId();
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }
}
