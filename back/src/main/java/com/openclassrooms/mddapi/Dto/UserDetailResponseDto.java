package com.openclassrooms.mddapi.Dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDetailResponseDto {
    private Integer id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TopicResponseDto> subscribedTopics;
}