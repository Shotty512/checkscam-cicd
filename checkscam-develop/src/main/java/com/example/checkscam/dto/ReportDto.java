package com.example.checkscam.dto;

import com.example.checkscam.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
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
}