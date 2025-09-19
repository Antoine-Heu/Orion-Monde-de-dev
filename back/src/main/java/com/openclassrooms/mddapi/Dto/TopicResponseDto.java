package com.openclassrooms.mddapi.Dto;

import lombok.Data;

@Data
public class TopicResponseDto {
    private Integer id;
    private String title;
    private String description;
}