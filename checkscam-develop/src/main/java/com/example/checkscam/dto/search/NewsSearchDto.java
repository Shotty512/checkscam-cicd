package com.example.checkscam.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NewsSearchDto extends SearchDto {
    private Long id;
    private String url;
    private LocalDateTime createdAt;
}
