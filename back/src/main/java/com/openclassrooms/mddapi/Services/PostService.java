package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.PostCreateDto;
import com.openclassrooms.mddapi.Dto.PostDto;
import com.openclassrooms.mddapi.Dto.PostsResponseDto;

public interface PostService {
    PostDto getPostByIdDto(Integer id);
    PostDto createPostDto(PostCreateDto postRequest, Integer authorId);
    PostsResponseDto getPostsByUserSubscriptionsDto(Integer userId);
}
