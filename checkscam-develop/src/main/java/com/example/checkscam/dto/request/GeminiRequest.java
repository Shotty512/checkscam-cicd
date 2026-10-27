package com.example.checkscam.dto.request;

import com.example.checkscam.dto.ContentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeminiRequest {
    private List<ContentDto> contents;
}
