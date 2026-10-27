package com.example.checkscam.service.impl;

import com.example.checkscam.dto.ChatBotResponseV2Dto;
import com.example.checkscam.dto.ContentDto;
import com.example.checkscam.dto.PartDto;
import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.dto.request.GeminiRequest;
import com.example.checkscam.dto.response.GeminiResponse;
import com.example.checkscam.service.*;
import com.example.checkscam.service.gemini.GeminiService;
import com.example.checkscam.util.DataUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.checkscam.constant.Constant.PROMPT_CHATBOT_IN_CHECKSCAM;
import static com.example.checkscam.constant.Constant.PROMPT_CHATBOT_IN_FIRST;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckScamServiceImpl implements CheckScamService {
    private final ScamStatsService scamStatsService;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    private static final Pattern CODE_FENCE_PATTERN =
            Pattern.compile("^```(?:json)?\\s*([\\s\\S]*?)\\s*```$", Pattern.MULTILINE);

    private String stripCodeFence(String raw) {
        Matcher m = CODE_FENCE_PATTERN.matcher(raw.trim());
        return m.find() ? m.group(1).trim() : raw.trim();
    }
    @Override
    public String checkScam(CheckScamRequestDto requestDto) throws JsonProcessingException {
        Object infoScam = null;
        switch (requestDto.getType()){
            case SDT -> {
                requestDto.setInfo(DataUtil.normalizePhoneNumber(requestDto.getInfo()));
                DataUtil.validatePhoneNumber(requestDto.getInfo());
                infoScam = scamStatsService.getPhoneScamStatsInfo(requestDto.getInfo());
            }
            case STK -> {
                requestDto.setInfo(DataUtil.normalizePhoneNumber(requestDto.getInfo()));
                DataUtil.validateBankAccount(requestDto.getInfo());
                infoScam = scamStatsService.getBankScamStatsInfo(requestDto.getInfo());
            }
            case URL -> {
                infoScam = scamStatsService.getUrlScamStatsInfo(DataUtil.extractFullDomain(requestDto.getInfo()));
            }
        }
        log.info( infoScam == null ? "khong co thong tin về :" + requestDto.getInfo() : infoScam.toString());
        if (!ObjectUtils.isEmpty(infoScam)){
            String message = PROMPT_CHATBOT_IN_CHECKSCAM + "Dữ liệu bạn cần chuyển đổi là: " + infoScam;
            ContentDto contentDto = new ContentDto();
            PartDto messagePart = new PartDto();
            messagePart.setText(message);
            contentDto.setParts(List.of(messagePart));
            GeminiRequest geminiRequest = new GeminiRequest();
            geminiRequest.setContents(List.of(contentDto));
            GeminiResponse responseData = geminiService.chatCompletions(geminiRequest);
            log.info("responseData: " + responseData.toString());
            return responseData.getCandidates()
                    .get(0).getContent().getParts().get(0).getText();
        } else
            return "Hiện tại không có thông tin lừa đảo về " + requestDto.getInfo();
    }

    @Override
    public String chatBot(String message) throws JsonProcessingException {
        ContentDto contentDto = new ContentDto();
        PartDto prompt = new PartDto();
        prompt.setText(PROMPT_CHATBOT_IN_FIRST);
        PartDto messagePart = new PartDto();
        messagePart.setText(message);
        contentDto.setParts(List.of(prompt, messagePart));
        GeminiRequest geminiRequest = new GeminiRequest();
        geminiRequest.setContents(List.of(contentDto));
        GeminiResponse responseData = geminiService.chatCompletions(geminiRequest);
        if (ObjectUtils.isEmpty(responseData)) {
            return "Chatbot có lỗi xảy ra xin vui lòng thử lại sau";
        } else {
            log.info("responseData: " + responseData);
            String raw = responseData.getCandidates()
                    .get(0).getContent().getParts().get(0).getText();
            String jsonString = stripCodeFence(raw);
            ChatBotResponseV2Dto chatBotResponseV2Dto = objectMapper.readValue(jsonString, ChatBotResponseV2Dto.class);
            if (ObjectUtils.isEmpty(chatBotResponseV2Dto.getContent())) {
                chatBotResponseV2Dto.setContent("Chatbot vừa xảy ra lỗi, vui lòng thử lại");
            }
            if (chatBotResponseV2Dto.getType() == 1){
                CheckScamRequestDto checkScamRequestDto = new CheckScamRequestDto();
                checkScamRequestDto.setInfo(chatBotResponseV2Dto.getContent());
                checkScamRequestDto.setType(chatBotResponseV2Dto.getTypeScam());
                return checkScam(checkScamRequestDto);
            }else {
                return chatBotResponseV2Dto.getContent();
            }
        }
    }
}
