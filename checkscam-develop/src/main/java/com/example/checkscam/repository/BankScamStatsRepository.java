package com.example.checkscam.repository;

import com.example.checkscam.dto.BankScamStatsInfoDto;
import com.example.checkscam.entity.BankScamStats;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankScamStatsRepository extends JpaRepository<BankScamStats, Long> {

    @Query("""
              SELECT new com.example.checkscam.dto.BankScamStatsInfoDto(
                       stats.id,
                       scam.bankAccount,
                       stats.lastReportAt,
                       stats.verifiedCount)
              FROM BankScamStats stats
              JOIN stats.bankScam scam
              WHERE scam.bankAccount = :bankAccount
            """)
    BankScamStatsInfoDto findByBankAccount(@Param("bankAccount") String bankAccount);

    @Query(value = """
            SELECT
            st.bank_scam_id AS id,
            st.verified_count AS verifiedCount,
            st.reasons_json AS reasonsJson,
            st.last_report_at AS lastReportAt,
            b.bank_account AS bankAccount
            FROM bank_scam_stats st 
            JOIN bank_scam b ON st.bank_scam_id = b.id
            ORDER BY st.verified_count DESC
            LIMIT 10
            """, nativeQuery = true)
    List<BankScamStatsInfo> getTopScamBank();
}