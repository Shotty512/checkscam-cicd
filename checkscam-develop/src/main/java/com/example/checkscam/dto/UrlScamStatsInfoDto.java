package com.example.checkscam.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
public class UrlScamStatsInfoDto {
    private Long id;
    private String urlScam;
    private Integer verifiedCount;
    private LocalDateTime lastReportAt;

    public UrlScamStatsInfoDto(Long id, String urlScam, Integer verifiedCount, LocalDateTime lastReportAt) {
        this.id = id;
        this.urlScam = urlScam;
        this.verifiedCount = verifiedCount;
        this.lastReportAt = lastReportAt;
    }

}
