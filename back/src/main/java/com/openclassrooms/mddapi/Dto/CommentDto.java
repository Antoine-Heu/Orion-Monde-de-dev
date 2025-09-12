package com.openclassrooms.mddapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String content;
    private Integer postId;
    private Integer authorId;
    private String authorName;
}