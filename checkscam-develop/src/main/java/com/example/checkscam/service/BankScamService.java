package com.example.checkscam.service;

import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BankScamService {
    void handleAfterReport(Report report, Long idScamType) throws CheckScamException;

    @Transactional
    void importBankData(MultipartFile file) throws IOException;
}
