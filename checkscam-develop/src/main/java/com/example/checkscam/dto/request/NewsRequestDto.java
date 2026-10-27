package com.example.checkscam.dto.request;

import com.example.checkscam.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDto {
    private String name;
    private String shortDescription;
    private String content;
    private List<Attachment> attachments;

}
