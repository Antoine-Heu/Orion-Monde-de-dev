package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Dto.TopicsResponseDto;
import com.openclassrooms.mddapi.Models.Topic;

public interface TopicService {
    void deleteTopic(Integer id);
    void subscribeToTopic(Integer userId, Integer topicId);
    void unsubscribeFromTopic(Integer userId, Integer topicId);
    TopicsResponseDto getAllTopicsDto();
    TopicResponseDto getTopicByIdDto(Integer id);
    TopicResponseDto createTopicDto(Topic topic);
    TopicResponseDto updateTopicDto(Integer id, Topic topicDetails);
    TopicsResponseDto getSubscribedTopicsDto(Integer userId);
}