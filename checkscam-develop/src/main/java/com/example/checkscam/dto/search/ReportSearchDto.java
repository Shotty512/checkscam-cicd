package com.example.checkscam.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportSearchDto extends SearchDto {
    private Long id;
    private String info;
    private String description;
    private Integer status;
    private Integer type;
    private Integer idScamTypeAfterHandle;
    private String emailAuthorReport;
    private String reason;
    private String infoDescription;
    private LocalDateTime dateReport;
}
