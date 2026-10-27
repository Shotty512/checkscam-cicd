package com.example.checkscam.entity;

import com.example.checkscam.converter.ReasonsJsonDtoConverter;
import com.example.checkscam.dto.ReasonsJsonDto;
import com.example.checkscam.dto.ScamStatsDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bank_scam_stats",
        indexes = {
                @Index(name = "idx_bank_cnt", columnList = "verified_count desc"),
                @Index(name = "idx_bank_last", columnList = "last_report_at desc")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankScamStats {

    /* Dùng PK chung với bank_scam */
    @Id
    @Column(name = "bank_scam_id")
    private Long id;

    @OneToOne
    @MapsId                                   // chia sẻ PK
    @JoinColumn(name = "bank_scam_id")
    private BankScam bankScam;

    @Column(name = "verified_count", nullable = false)
    private Integer verifiedCount = 0;

    @Convert(converter = ReasonsJsonDtoConverter.class)
    @Column(name = "reasons_json", columnDefinition = "json")
    private ReasonsJsonDto reasonsJson;

    @Column(name = "last_report_at")
    private LocalDateTime lastReportAt;

    public BankScamStats(ScamStatsDto stats) {
        this.id = stats.getId();
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }

    public void apply(ScamStatsDto stats) {
        this.verifiedCount = stats.getVerifiedCount();
        this.reasonsJson = stats.getReasonsJson();
        this.lastReportAt = stats.getLastReportAt();
    }
}
