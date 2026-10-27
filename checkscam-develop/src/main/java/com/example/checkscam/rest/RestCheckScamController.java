package com.example.checkscam.rest;

import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.CheckScamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/check-scam")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class RestCheckScamController {
    private final CheckScamService checkScamService;

    @PostMapping
    public CheckScamResponse<String> checkScam(@RequestBody @NotNull CheckScamRequestDto requestDto) throws JsonProcessingException {
        return new CheckScamResponse<>(this.checkScamService.checkScam(requestDto));
    }

    @PostMapping("/chat")
    public CheckScamResponse<String> chat(@RequestBody String message) throws JsonProcessingException {
        return new CheckScamResponse<>(this.checkScamService.chatBot(message));
    }
}
