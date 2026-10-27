package com.example.checkscam.entity;

import com.example.checkscam.converter.ReasonsJsonDtoConverter;
import com.example.checkscam.dto.ReasonsJsonDto;
import com.example.checkscam.dto.ScamStatsDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_scam_stats",
        indexes = {
                @Index(name = "idx_url_cnt", columnList = "verified_count desc"),
                @Index(name = "idx_url_last", columnList = "last_report_at desc")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlScamStats {

    @Id
    @Column(name = "url_scam_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "url_scam_id")
    private UrlScam urlScam;

    @Column(name = "verified_count", nullable = false)
    private Integer verifiedCount = 0;

    @Convert(converter = ReasonsJsonDtoConverter.class)
    @Column(name = "reasons_json", columnDefinition = "json")
    private ReasonsJsonDto reasonsJson;

    @Column(name = "last_report_at")
    private LocalDateTime lastReportAt;

    public UrlScamStats(ScamStatsDto stats) {
        this.id = stats.getId();
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }
}