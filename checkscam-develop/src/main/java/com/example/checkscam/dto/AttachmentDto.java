package com.example.checkscam.dto;

import com.example.checkscam.entity.Attachment;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AttachmentDto {
    private Long id;
    private String url;
    private ReportDto report;
    private NewsDto news;

    public AttachmentDto(Attachment attachment) {
        this.id = attachment.getId();
        this.url = attachment.getUrl();
    }
}