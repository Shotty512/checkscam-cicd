package com.example.checkscam.service.impl;

import com.example.checkscam.component.FileUtils;
import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.constant.ScamInfoType;
import com.example.checkscam.constant.StatusReportEnum;
import com.example.checkscam.dto.AttachmentDto;
import com.example.checkscam.dto.MonthlyReportStatsDto;
import com.example.checkscam.dto.YearlyReportStatsDto;
import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.dto.search.ReportSearchDto;
import com.example.checkscam.entity.Attachment;
import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.exception.FileUploadValidationException;
import com.example.checkscam.exception.InvalidParamException;
import com.example.checkscam.repository.AttachmentRepository;
import com.example.checkscam.repository.ReportRepository;
import com.example.checkscam.repository.projection.ReportInfo;
import com.example.checkscam.service.BankScamService;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ReportService;
import com.example.checkscam.service.UrlScamService;
import com.example.checkscam.service.mapper.AttachmentMapper;
import com.example.checkscam.service.mapper.ReportMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final ReportMapper mapper;
    private final BankScamService bankScamService;
    private final PhoneScamService phoneScamService;
    private final UrlScamService urlScamService;
    private final AttachmentRepository attachmentRepository;
    private final FileUtils fileUtils;
    private final AttachmentMapper attachmentMapper;

    public static final int MAXIMUM_ATTACHMENTS_PER_REPORT = 5;

    @Override
    public ReportResponseDto createReport(ReportRequestDto request) {
        Report report = mapper.requestToEntity(request);
        report.setDateReport(LocalDateTime.now());
        report.setStatus(StatusReportEnum.PENDING.getType());
        return mapper.entityToResponse(repository.save(report));
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        log.info("Get all reports");
        List<Report> reports = repository.findAll();
        return reports.stream()
                .map(mapper::entityToResponse)
                .toList();
    }

    @Override
    public ReportResponseDto updateReport(Long id, ReportRequestDto request) throws CheckScamException {
        log.info("Update report with id {}", id);
        Report report = repository.findById(id).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        mapper.setValue(request, report);
        return mapper.entityToResponse(repository.save(report));
    }

    @Override
    public ReportResponseDto getById(Long id) throws CheckScamException {
        log.info("Get report with id {}", id);
        Report report = repository.findById(id).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        ReportResponseDto reportResponseDto = mapper.entityToResponse(report);
        if (report.getAttachments() != null) {
            List<AttachmentDto> attachmentDtos = report.getAttachments().stream()
                    .map(attachmentMapper::entityToDto)
                    .collect(Collectors.toList());
            reportResponseDto.setAttachmentDto(attachmentDtos);
        }
        return reportResponseDto;
    }

    @Override
    public Page<ReportResponseDto> searchReports(ReportSearchDto searchDto) {
        Pageable pageable = PageRequest.of(searchDto.getPageIndex() - 1, searchDto.getPageSize());
        return repository.search(searchDto, pageable);
    }

    @Override
    public List<ReportInfo> findTop10RepeatedInfoByType(Integer type) {
        return repository.findTop10RepeatedInfoByType(type);
    }

    @Override
    public ReportResponseDto handleReport(HandleReportRequestDto requestDto) throws CheckScamException {
        log.info("Handle report with id {}", requestDto.getIdReport());
        Report report = repository.findById(requestDto.getIdReport()).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        if (!StatusReportEnum.PENDING.getType().equals(report.getStatus())) {
            throw new RuntimeException("Chỉ xử lý báo cáo có trạng thái PENDING");
        }
        report.setStatus(requestDto.getStatus());
        report.setIdScamTypeAfterHandle(requestDto.getIdScamTypeAfterHandle());

        if (StatusReportEnum.APPROVED.getType().equals(requestDto.getStatus())) {
            if (ScamInfoType.STK.getType().equals(report.getType())) {
                bankScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
            if (ScamInfoType.SDT.getType().equals(report.getType())) {
                phoneScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
            if (ScamInfoType.URL.getType().equals(report.getType())) {
                urlScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
        }

        return mapper.entityToResponse(repository.save(report));
    }


    @Transactional
    @Override
    public List<Attachment> uploadAndCreateAttachments(Long reportId,
                                                       List<MultipartFile> files) throws Exception {

        Report report = repository.findById(reportId)
                .orElseThrow(() -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));

        List<MultipartFile> validFiles =
                (files == null ? List.<MultipartFile>of() : files).stream()
                        .filter(f -> f != null && !f.isEmpty() && f.getSize() > 0)
                        .toList();

        if (validFiles.isEmpty()) {
            return List.of();
        }

        int current = attachmentRepository.countByReportId(reportId);
        if (current + validFiles.size() > MAXIMUM_ATTACHMENTS_PER_REPORT) {
            throw new InvalidParamException(String.format(
                    "Report %d đã có %d file, tối đa %d. Không thể thêm %d file.",
                    reportId, current, MAXIMUM_ATTACHMENTS_PER_REPORT, validFiles.size()));
        }

        List<Attachment> saved = new ArrayList<>();

        for (MultipartFile file : validFiles) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new FileUploadValidationException(
                        "Kích thước file vượt quá 10MB: " + file.getOriginalFilename(),
                        HttpStatus.PAYLOAD_TOO_LARGE);
            }
            if (file.getContentType() == null
                    || !file.getContentType().startsWith("image/")) {
                throw new FileUploadValidationException(
                        "File phải là hình ảnh: " + file.getOriginalFilename(),
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            String storedName = fileUtils.storeFile(file);

            Attachment toSave = Attachment.builder()
                    .report(report)
                    .url(storedName)
                    .build();

            saved.add(attachmentRepository.save(toSave));
        }

        return saved;
    }

    @Override
    public Resource loadImage(String imageName) throws IOException {
        validateImageName(imageName);

        Path imagePath = fileUtils.resolve(imageName);

        if (!Files.exists(imagePath)) {
            Path fallback = fileUtils.resolve("notfound.jpeg");
            if (Files.exists(fallback)) {
                return new UrlResource(fallback.toUri());
            }
            throw new FileNotFoundException("Image not found: " + imageName);
        }
        return new UrlResource(imagePath.toUri());
    }

    @Override
    public String getImageMimeType(String imageName) throws IOException {
        Path path = fileUtils.resolve(imageName);
        if (!Files.exists(path)) {
            path = fileUtils.resolve("notfound.jpeg");
        }
        return Files.probeContentType(path);
    }

    private void validateImageName(String imageName) {
        if (imageName.contains("..") || imageName.contains("/") || imageName.contains("\\")) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }
    }

    @Override
    public List<MonthlyReportStatsDto> getMonthlyStats(int year) {
        return repository.findReportCountByMonth(year);
    }

    @Override
    public List<YearlyReportStatsDto> getYearlyStats() {
        return repository.findReportCountByYear();
    }
}