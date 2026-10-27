package com.example.checkscam.repository;

import com.example.checkscam.dto.PhoneScamStatsInfoDto;
import com.example.checkscam.entity.PhoneScamStats;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneScamStatsRepository extends JpaRepository<PhoneScamStats, Long> {

    @Query("""
              SELECT new com.example.checkscam.dto.PhoneScamStatsInfoDto(
                       scam.id,
                       scam.phoneNumber,
                       stats.lastReportAt,
                       stats.verifiedCount,
                       stats.reasonsJson)
              FROM PhoneScamStats stats
              JOIN stats.phoneScam scam
              WHERE scam.phoneNumber = :phoneNumber
            """)
    PhoneScamStatsInfoDto findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query(value = """
            SELECT
            st.phone_scam_id AS id,
            st.verified_count AS verifiedCount,
            st.reasons_json AS reasonsJson,
            st.last_report_at AS lastReportAt,
            p.phone_number AS phoneNumber
            FROM phone_scam_stats st 
            JOIN phone_scam p ON st.phone_scam_id = p.id
            ORDER BY st.verified_count DESC
            LIMIT 10
            """, nativeQuery = true)
    List<PhoneScamStatsInfo> getTopScamPhone();

}