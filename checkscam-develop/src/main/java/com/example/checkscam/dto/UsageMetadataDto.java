package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsageMetadataDto {
    private int promptTokenCount;
    private int candidatesTokenCount;
    private int totalTokenCount;
    private List<TokenDetailsDto> promptTokensDetails;
    private List<TokenDetailsDto> candidatesTokensDetails;
}

