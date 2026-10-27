package com.example.checkscam.dto.response;

import com.example.checkscam.dto.CandidateDto;
import com.example.checkscam.dto.UsageMetadataDto;
import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<CandidateDto> candidates;
    private UsageMetadataDto usageMetadata;
    private String modelVersion;

    public List<CandidateDto> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateDto> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadataDto getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadataDto usageMetadata) {
        this.usageMetadata = usageMetadata;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }
}