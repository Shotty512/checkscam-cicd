package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_scam",
        uniqueConstraints = @UniqueConstraint(name = "idx_phone_number", columnNames = "phone_number"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneScam extends BaseEntity {

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "phoneScam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PhoneScamStats stats;
}
