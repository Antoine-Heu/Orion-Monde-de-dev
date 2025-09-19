package com.openclassrooms.mddapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String content;
    private Integer postId;
    private Integer authorId;
    private String authorName;
}