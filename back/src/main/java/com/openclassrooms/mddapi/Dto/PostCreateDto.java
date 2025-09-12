package com.openclassrooms.mddapi.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostCreateDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private Integer topicId;
}