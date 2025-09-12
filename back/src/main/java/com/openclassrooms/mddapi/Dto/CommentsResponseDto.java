package com.openclassrooms.mddapi.Dto;

import lombok.Data;
import java.util.List;

@Data
public class CommentsResponseDto {
    private List<CommentDto> comments;
}