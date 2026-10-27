package com.example.checkscam.dto;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankScamDto {
    private Long id;
    private String bankAccount;
    private String description;
    private String bankName;
    private String nameAccount;
}