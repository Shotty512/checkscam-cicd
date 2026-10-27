package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyReportStatsDto {
    private int month;
    private long count;
}
