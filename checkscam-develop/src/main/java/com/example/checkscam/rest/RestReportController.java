package com.example.checkscam.rest;

import com.example.checkscam.component.LocalizationUtils;
import com.example.checkscam.constant.MessageKeys;
import com.example.checkscam.dto.MonthlyReportStatsDto;
import com.example.checkscam.dto.YearlyReportStatsDto;
import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.dto.search.ReportSearchDto;
import com.example.checkscam.entity.Attachment;
import com.example.checkscam.exception.*;
import com.example.checkscam.repository.projection.ReportInfo;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.response.ResponseObject;
import com.example.checkscam.service.ReportService;
import com.example.checkscam.service.impl.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class RestReportController {
    private final ReportService reportService;
    private final LocalizationUtils localizationUtils;
    private final CaptchaService captchaService;

    @PostMapping
    public CheckScamResponse<ReportResponseDto> createReport(@RequestBody ReportRequestDto request) {
        boolean valid = captchaService.verify(request.getCaptchaToken());
        if (!valid) {
            throw new InvalidCaptchaException();
        }
        return new CheckScamResponse<>(reportService.createReport(request));
    }

    @PutMapping("/{id}")
    public CheckScamResponse<ReportResponseDto> updateReport(@PathVariable Long id,@RequestBody ReportRequestDto request) throws CheckScamException {
        return new CheckScamResponse<>(reportService.updateReport(id, request));
    }

    @PostMapping("/handle")
    public CheckScamResponse<ReportResponseDto> handleReport(@RequestBody HandleReportRequestDto request) throws CheckScamException {
        return new CheckScamResponse<>(reportService.handleReport(request));
    }

    @GetMapping("/{id}")
    public CheckScamResponse<ReportResponseDto> getById(@PathVariable Long id) throws CheckScamException {
        return new CheckScamResponse<>(reportService.getById(id));
    }

    @GetMapping("/top")
    public CheckScamResponse<List<ReportInfo>> getTopReportsByType(@RequestParam Integer type) {
        return new CheckScamResponse<>(reportService.findTop10RepeatedInfoByType(type));
    }

    @GetMapping("/all")
    public CheckScamResponse<List<ReportResponseDto>> getAllReportsByType() {
        return new CheckScamResponse<>(reportService.getAllReports());
    }
    @GetMapping("/search")
    public CheckScamResponse<Page<ReportResponseDto>> search(ReportSearchDto searchDto) {
        return new CheckScamResponse<>(reportService.searchReports(searchDto));
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> uploadAttachments(
            @PathVariable("id") Long reportId,
            @RequestParam("files")List<MultipartFile> files) {
        try {
            List<Attachment> attachments = reportService.uploadAndCreateAttachments(reportId, files);
            if (attachments.isEmpty() && (files == null || files.stream().allMatch(f -> f == null || f.isEmpty() || f.getSize() == 0))) {
                return ResponseEntity.ok().body(ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_NO_VALID_FILES))
                        .status(HttpStatus.OK)
                        .data(attachments)
                        .build());
            }

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_SUCCESSFULLY))
                    .status(HttpStatus.OK)
                    .data(attachments)
                    .build());

        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessage()))
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } catch (InvalidParamException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessage()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (FileUploadValidationException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(e.getMessageKey(), e.getArgs()))
                    .status(e.getHttpStatus())
                    .build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.UPLOAD_ATTACHMENTS_FILE_STORAGE_ERROR, e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.ERROR_OCCURRED_DEFAULT, e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Resource image = reportService.loadImage(imageName);
            String mimeType = reportService.getImageMimeType(imageName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"))
                    .body(image);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tên file không hợp lệ");
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tải ảnh");
        }
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyReportStatsDto>> getMonthlyStats(
            @RequestParam int year
    ) {
        List<MonthlyReportStatsDto> stats = reportService.getMonthlyStats(year);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/yearly")
    public ResponseEntity<List<YearlyReportStatsDto>> getYearlyStats() {
        List<YearlyReportStatsDto> stats = reportService.getYearlyStats();
        return ResponseEntity.ok(stats);
    }
}
