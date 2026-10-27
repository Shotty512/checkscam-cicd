package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_scam",
        indexes = @Index(name = "idx_bank_account", columnList = "bank_account"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankScam extends BaseEntity {

    @Column(name = "bank_account", length = 34, nullable = false)
    private String bankAccount;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "name_bank")
    private String bankName;

    @Column(name = "name_account")
    private String nameAccount;

    /* 1‑1 ngược chiều (BankScamStats chứa FK & PK) */
    @OneToOne(mappedBy = "bankScam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BankScamStats stats;
}
