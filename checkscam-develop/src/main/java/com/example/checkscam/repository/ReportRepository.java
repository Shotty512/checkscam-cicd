package com.example.checkscam.repository;

import com.example.checkscam.dto.MonthlyReportStatsDto;
import com.example.checkscam.dto.YearlyReportStatsDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.dto.search.ReportSearchDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.repository.projection.ReportInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query(value = """
            WITH ranked AS (
                SELECT
                    r.id,
                    r.info,
                    r.description,
                    r.status,
                    r.type,
                    r.id_scam_type_before_handle AS idScamTypeAfterHandle,
                    r.email_author_report       AS emailAuthorReport,
                    r.reason,
                    r.info_description          AS infoDescription,
                    r.date_report               AS dateReport,
            
                    COUNT(*) OVER (PARTITION BY r.info) AS quantity,
            
                    ROW_NUMBER() OVER (
                        PARTITION BY r.info
                        ORDER BY r.date_report DESC
                    ) AS rn
                FROM report r
                WHERE r.info IS NOT NULL
                  AND r.type = :typeParam           
            )
            SELECT
                id,
                quantity,
                info,
                description,
                status,
                type,
                idScamTypeAfterHandle,
                emailAuthorReport,
                reason,
                infoDescription,
                dateReport
            FROM ranked
            WHERE rn = 1                  
              AND quantity > 1            
            ORDER BY quantity DESC
            LIMIT 10                       
            """, nativeQuery = true)
    List<ReportInfo> findTop10RepeatedInfoByType(@Param("typeParam") int type);


    @Query(
            value = """
            SELECT new com.example.checkscam.dto.response.ReportResponseDto(r) 
            FROM Report r
            WHERE 1 = 1 AND
                  (:#{#dto.info} IS NULL
                   OR lower(r.info) LIKE lower(concat('%', :#{#dto.info}, '%')))
              AND (:#{#dto.type} IS NULL
                   OR r.type = :#{#dto.type}) 
              AND (:#{#dto.status} IS NULL
                   OR r.status = :#{#dto.status}) 
            ORDER BY r.dateReport DESC
            """,
            countQuery = """
            SELECT COUNT(r)
            FROM Report r
            WHERE 1 = 1 AND
                  (:#{#dto.info} IS NULL
                   OR lower(r.info) LIKE lower(concat('%', :#{#dto.info}, '%')))
              AND (:#{#dto.type} IS NULL
                   OR r.type = :#{#dto.type})
              AND (:#{#dto.status} IS NULL
                   OR r.status = :#{#dto.status}) 
            """
    )
    Page<ReportResponseDto> search(@Param("dto") ReportSearchDto dto, Pageable pageable);

    @Query("""
      SELECT new com.example.checkscam.dto.MonthlyReportStatsDto(
        MONTH(r.dateReport),
        COUNT(r)
      )
      FROM Report r
      WHERE YEAR(r.dateReport) = :year
      GROUP BY MONTH(r.dateReport)
      ORDER BY MONTH(r.dateReport)
    """)
    List<MonthlyReportStatsDto> findReportCountByMonth(@Param("year") int year);

    @Query("""
      SELECT new com.example.checkscam.dto.YearlyReportStatsDto(
        YEAR(r.dateReport),
        COUNT(r)
      )
      FROM Report r
      GROUP BY YEAR(r.dateReport)
      ORDER BY YEAR(r.dateReport)
    """)
    List<YearlyReportStatsDto> findReportCountByYear();
}