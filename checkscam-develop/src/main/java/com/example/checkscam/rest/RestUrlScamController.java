package com.example.checkscam.rest;

import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.CheckScamService;
import com.example.checkscam.service.UrlScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/url-scam")
@RequiredArgsConstructor
public class RestUrlScamController {
    private final UrlScamService urlScamService;
    @PostMapping("/import")
    public CheckScamResponse<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            urlScamService.importUrlData(file);
            return new CheckScamResponse<>("Import thành công");
        } catch (Exception e) {
            return new CheckScamResponse<>("Import thất bại: " + e.getMessage());
        }
    }
}
