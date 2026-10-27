package com.example.checkscam.dto.response;

import com.example.checkscam.dto.AttachmentDto;
import com.example.checkscam.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private Long id;
    private String info;
    private String description;
    private Integer status;
    private Integer type;
    private Long idScamTypeAfterHandle;
    private String emailAuthorReport;
    private String reason;
    private String infoDescription;
    private LocalDateTime dateReport;
    private List<AttachmentDto> attachmentDto;

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.info = report.getInfo();
        this.description = report.getDescription();
        this.status = report.getStatus();
        this.type = report.getType();
        this.idScamTypeAfterHandle = report.getIdScamTypeAfterHandle();
        this.emailAuthorReport = report.getEmailAuthorReport();
        this.reason = report.getReason();
        this.infoDescription = report.getInfoDescription();
        this.dateReport = report.getDateReport();
        if (report.getAttachments() != null) {
            this.attachmentDto = report.getAttachments().stream()
                    .map(AttachmentDto::new)
                    .toList();
        }
    }
}
