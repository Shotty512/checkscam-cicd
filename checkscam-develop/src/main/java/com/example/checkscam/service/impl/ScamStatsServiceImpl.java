package com.example.checkscam.service.impl;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.dto.*;
import com.example.checkscam.entity.Report;
import com.example.checkscam.entity.ScamTypes;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.BankScamStatsRepository;
import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.repository.UrlScamStatsRepository;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;
import com.example.checkscam.service.ScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScamStatsServiceImpl implements ScamStatsService {
    private final UrlScamStatsRepository urlRepository;
    private final PhoneScamStatsRepository phoneRepository;
    private final BankScamStatsRepository bankRepository;
    private final ScamTypesRepository scamTypesRepository;

    @Override
    public BankScamStatsInfoDto getBankScamStatsInfo(String bankAccount) {
        return bankRepository.findByBankAccount(bankAccount);
    }

    @Override
    public PhoneScamStatsInfoDto getPhoneScamStatsInfo(String phoneNumber) {
        return phoneRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UrlScamStatsInfoDto getUrlScamStatsInfo(String url) {
        return urlRepository.findByUrlScam(url);
    }

    @Override
    public List<PhoneScamStatsInfo> getPhoneScamStatsInfoList() {
        return phoneRepository.getTopScamPhone();
    }

    @Override
    public List<UrlScamStatsInfo> getUrlScamStatsInfoList() {
        return urlRepository.getTopScamUrl();
    }

    @Override
    public List<BankScamStatsInfo> getBankScamStatsInfoList() {
        return bankRepository.getTopScamBank();
    }

    @Override
    public ScamStatsDto handleStats(ScamStatsDto stats, Report report, Long idScamType)
            throws CheckScamException {

        if (stats == null) {
            stats = new ScamStatsDto();
        }

        int verified = Optional.ofNullable(stats.getVerifiedCount()).orElse(0);
        stats.setVerifiedCount(verified + 1);

        stats.setLastReportAt(report.getDateReport());

        if (!ObjectUtils.isEmpty(idScamType)){
            ScamTypes scamType = scamTypesRepository.findById(idScamType).orElseThrow(
                    () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND)
            );

            ReasonsJsonDto reasonsJson = Optional.ofNullable(stats.getReasonsJson())
                    .orElseGet(ReasonsJsonDto::new);
            List<ReasonsJsonDto.Reason> reasons = Optional.ofNullable(reasonsJson.getReasons())
                    .orElseGet(ArrayList::new);

            ReasonsJsonDto.Reason reason = reasons.stream()
                    .filter(r -> r.isExitByName(scamType.getName()))
                    .findFirst()
                    .orElse(null);
            if (reason == null) {
                reason = new ReasonsJsonDto.Reason();
                reason.setName(scamType.getName());
                reason.setQuantity(1);
                reasons.add(reason);
            } else {
                reason.setQuantity(reason.getQuantity() + 1);
            }

            reasonsJson.setReasons(reasons);
            stats.setReasonsJson(reasonsJson);
        }

        return stats;
    }

}
