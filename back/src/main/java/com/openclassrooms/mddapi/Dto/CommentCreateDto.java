package com.openclassrooms.mddapi.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentCreateDto {
    @NotEmpty
    @Size(min = 3, message = "Le commentaire doit contenir au moins 3 caractères")
    private String content;

    @NotNull
    private Integer postId;
}