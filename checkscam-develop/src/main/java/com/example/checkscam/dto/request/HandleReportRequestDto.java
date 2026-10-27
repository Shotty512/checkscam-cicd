package com.example.checkscam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HandleReportRequestDto {
    private Long idReport;
    private Integer status;
    private Long idScamTypeAfterHandle;
}
