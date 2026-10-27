package com.example.checkscam.service;

import com.example.checkscam.dto.BankScamStatsInfoDto;
import com.example.checkscam.dto.PhoneScamStatsInfoDto;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.dto.UrlScamStatsInfoDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;

import java.util.List;

public interface ScamStatsService {
    BankScamStatsInfoDto getBankScamStatsInfo(String bankAccount);

    PhoneScamStatsInfoDto getPhoneScamStatsInfo(String phoneNumber);

    UrlScamStatsInfoDto getUrlScamStatsInfo(String url);

    List<PhoneScamStatsInfo> getPhoneScamStatsInfoList();

    List<UrlScamStatsInfo> getUrlScamStatsInfoList();

    List<BankScamStatsInfo> getBankScamStatsInfoList();

    ScamStatsDto handleStats(ScamStatsDto stats, Report report, Long idScamType) throws CheckScamException;
}
