package com.example.checkscam.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatBotResponseV2Dto {
    private Integer type = 2;
    private String content;
    private Integer typeScam;
}
