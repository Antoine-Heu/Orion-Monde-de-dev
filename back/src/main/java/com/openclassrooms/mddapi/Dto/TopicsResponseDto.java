package com.openclassrooms.mddapi.Dto;

import lombok.Data;
import java.util.List;

@Data
public class TopicsResponseDto {
    private List<TopicResponseDto> topics;
}