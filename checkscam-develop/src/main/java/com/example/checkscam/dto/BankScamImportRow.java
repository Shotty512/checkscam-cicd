package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankScamImportRow {
    private String bankAccount;
    private String nameAccount;
    private String bankName;
}