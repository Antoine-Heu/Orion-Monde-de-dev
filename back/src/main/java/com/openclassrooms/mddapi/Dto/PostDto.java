package com.openclassrooms.mddapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer topicId;
    private String topicTitle;
    private Integer authorId;
    private String authorName;
}