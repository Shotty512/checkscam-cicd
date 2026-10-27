package com.example.checkscam.service;

import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhoneScamService {
    void handleAfterReport(Report report, Long idScamType) throws CheckScamException;

    @Transactional
    void importData(MultipartFile file) throws IOException;
}
