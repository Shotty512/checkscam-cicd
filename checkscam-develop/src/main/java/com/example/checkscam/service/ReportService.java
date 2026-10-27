package com.example.checkscam.service;

import com.example.checkscam.dto.AttachmentDto;
import com.example.checkscam.dto.MonthlyReportStatsDto;
import com.example.checkscam.dto.YearlyReportStatsDto;
import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.dto.search.ReportSearchDto;
import com.example.checkscam.entity.Attachment;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.projection.ReportInfo;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto request);

    List<ReportResponseDto> getAllReports();

    ReportResponseDto updateReport(Long id, ReportRequestDto request) throws CheckScamException;

    ReportResponseDto getById(Long id) throws CheckScamException;

    Page<ReportResponseDto> searchReports(ReportSearchDto searchDto);

    List<ReportInfo> findTop10RepeatedInfoByType(Integer type);

    ReportResponseDto handleReport(HandleReportRequestDto requestDto) throws CheckScamException;

    @Transactional
    List<Attachment> uploadAndCreateAttachments(Long reportId, List<MultipartFile> files) throws Exception;

    Resource loadImage(String imageName) throws IOException;

    String getImageMimeType(String imageName) throws IOException;

    List<MonthlyReportStatsDto> getMonthlyStats(int year);

    List<YearlyReportStatsDto> getYearlyStats();
}
