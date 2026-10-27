package com.example.checkscam.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UrlScamDto {
    private Long id;
    private String info;
    private String description;
}