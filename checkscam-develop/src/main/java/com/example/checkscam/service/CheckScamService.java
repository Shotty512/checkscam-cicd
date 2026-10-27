package com.example.checkscam.service;

import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CheckScamService {
    String checkScam(CheckScamRequestDto requestDto) throws JsonProcessingException;

    String chatBot(String message) throws JsonProcessingException;
}
