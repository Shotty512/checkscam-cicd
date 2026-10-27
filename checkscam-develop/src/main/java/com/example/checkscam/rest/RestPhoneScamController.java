package com.example.checkscam.rest;

import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/phone-scam")
@RequiredArgsConstructor
public class RestPhoneScamController {
    private final PhoneScamService phoneScamService;
    private final ScamStatsService scamStatsService;

    @PostMapping("/import")
    public CheckScamResponse<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            phoneScamService.importData(file);
            return new CheckScamResponse<>("Import thành công");
        } catch (Exception e) {
            return new CheckScamResponse<>("Import thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/top")
    public CheckScamResponse<?> getTopPhoneScam() {
            return new CheckScamResponse<>(scamStatsService.getPhoneScamStatsInfoList());
    }
}
