package com.example.checkscam.repository;

import com.example.checkscam.dto.UrlScamStatsInfoDto;
import com.example.checkscam.entity.UrlScamStats;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlScamStatsRepository extends JpaRepository<UrlScamStats, Long> {

    @Query("""
              SELECT new com.example.checkscam.dto.UrlScamStatsInfoDto(
                       stats.id,
                       scam.info,
                       stats.verifiedCount,
                       stats.lastReportAt)
              FROM UrlScamStats stats
              JOIN stats.urlScam scam
              WHERE scam.info = :scamUrl
            """)
    UrlScamStatsInfoDto findByUrlScam(String scamUrl);

    @Query(value = """
            SELECT
            st.url_scam_id AS id,
            st.verified_count AS verifiedCount,
            st.last_report_at AS lastReportAt,
            u.info AS url
            FROM url_scam_stats st 
            JOIN url_scam u ON st.url_scam_id = u.id
            ORDER BY st.verified_count DESC
            LIMIT 10
            """, nativeQuery = true)
    List<UrlScamStatsInfo> getTopScamUrl();
}