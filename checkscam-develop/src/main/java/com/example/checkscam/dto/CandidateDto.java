package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CandidateDto {
    private CandidateContentDto content;
    private String finishReason;
    private double avgLogprobs;
}
