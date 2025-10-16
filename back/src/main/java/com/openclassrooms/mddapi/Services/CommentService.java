package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.CommentCreateDto;
import com.openclassrooms.mddapi.Dto.CommentDto;
import com.openclassrooms.mddapi.Dto.CommentsResponseDto;

public interface CommentService {

    CommentDto createCommentDto(CommentCreateDto commentRequest, Integer authorId);
    CommentsResponseDto getCommentsByPostIdDto(Integer postId);
}