package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.CommentCreateDto;
import com.openclassrooms.mddapi.Dto.CommentDto;
import com.openclassrooms.mddapi.Dto.CommentsResponseDto;
import com.openclassrooms.mddapi.Models.Comment;

import java.util.List;
import java.util.stream.Collectors;

public interface CommentService {

    CommentDto createCommentDto(CommentCreateDto commentRequest, Integer authorId);
    CommentsResponseDto getCommentsByPostIdDto(Integer postId);
}