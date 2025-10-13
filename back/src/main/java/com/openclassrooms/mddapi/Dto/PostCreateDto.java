package com.openclassrooms.mddapi.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostCreateDto {
    @NotEmpty
    @Size(min = 3, message = "Le titre doit contenir au moins 3 caractères")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    private String content;

    @NotNull
    private Integer topicId;
}