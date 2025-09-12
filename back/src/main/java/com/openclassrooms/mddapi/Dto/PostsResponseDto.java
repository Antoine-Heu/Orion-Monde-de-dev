package com.openclassrooms.mddapi.Dto;

import lombok.Data;
import java.util.List;

@Data
public class PostsResponseDto {
    private List<PostDto> posts;
}