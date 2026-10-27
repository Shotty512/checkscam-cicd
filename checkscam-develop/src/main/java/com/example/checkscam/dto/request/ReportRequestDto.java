package com.example.checkscam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {
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
    private String captchaToken;
}
