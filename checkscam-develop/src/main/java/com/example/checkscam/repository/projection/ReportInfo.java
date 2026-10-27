package com.example.checkscam.repository.projection;

import java.time.LocalDateTime;

public interface ReportInfo {
    Long getId();

    Integer getQuantity();

    String getInfo();

    String getDescription();

    Integer getStatus();

    Integer getType();

    Long getIdScamTypeAfterHandle();

    String getEmailAuthorReport();

    String getReason();

    String getInfoDescription();

    LocalDateTime getDateReport();
}