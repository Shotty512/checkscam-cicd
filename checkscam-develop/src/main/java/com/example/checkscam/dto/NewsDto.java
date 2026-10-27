package com.example.checkscam.dto;

import com.example.checkscam.entity.News;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NewsDto {
    private Long id;
    private String name;
    private String shortDescription;
    private String content;
    private List<AttachmentDto> attachments;

    public NewsDto(News news) {
        this.id = news.getId();
        this.name = news.getName();
        this.shortDescription = news.getShortDescription();
        this.content = news.getContent();
        if (news.getAttachments() != null) {
            this.attachments = news.getAttachments().stream()
                    .map(AttachmentDto::new)
                    .toList();
        }
    }
}