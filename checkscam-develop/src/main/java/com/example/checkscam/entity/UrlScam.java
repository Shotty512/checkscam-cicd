package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "url_scam",
        indexes = @Index(name = "idx_url", columnList = "info"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlScam extends BaseEntity {

    @Column(name = "info", length = 512, nullable = false)
    private String info;            // URL

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "urlScam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UrlScamStats stats;
}
