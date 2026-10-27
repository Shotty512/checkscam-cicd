package com.example.checkscam.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PhoneScamDto {
    private Long id;
    private String phoneNumber;
    private String description;
}