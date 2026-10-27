package com.example.checkscam.service.gemini;

import com.example.checkscam.dto.request.GeminiRequest;
import com.example.checkscam.dto.response.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GeminiService {
    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeminiResponse chatCompletions(GeminiRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        StringBuilder url = new StringBuilder(apiUrl);
        if (!ObjectUtils.isEmpty(apiKey)) {
             url.append("?key=");
             url.append(apiKey);
        } else {
            log.error("Gemini API Key is not configured!");
        }
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
        try{
            return restTemplate.postForObject(
                    url.toString(),
                    entity,
                    GeminiResponse.class
            );
        } catch (Exception e) {
            log.error("Error calling OpenRouter API: {}", e.getMessage());
        }
        return null;
    }
}
