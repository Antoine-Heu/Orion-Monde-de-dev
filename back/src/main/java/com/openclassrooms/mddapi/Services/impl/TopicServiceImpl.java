package com.openclassrooms.mddapi.Services.impl;

import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Dto.TopicsResponseDto;
import com.openclassrooms.mddapi.Models.Topic;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.TopicRepository;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import com.openclassrooms.mddapi.Services.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void deleteTopic(Integer id) {
        topicRepository.deleteById(id);
    }

    @Override
    public void subscribeToTopic(Integer userId, Integer topicId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Topic> topic = topicRepository.findById(topicId);

        if (user.isPresent() && topic.isPresent()) {
            User u = user.get();
            Topic t = topic.get();

            if (!u.getSubscribedTopics().contains(t)) {
                u.getSubscribedTopics().add(t);
                userRepository.save(u);
            }
        } else {
            throw new RuntimeException("User or Topic not found");
        }
    }

    @Override
    public void unsubscribeFromTopic(Integer userId, Integer topicId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Topic> topic = topicRepository.findById(topicId);

        if (user.isPresent() && topic.isPresent()) {
            User u = user.get();
            Topic t = topic.get();

            u.getSubscribedTopics().remove(t);
            userRepository.save(u);
        } else {
            throw new RuntimeException("User or Topic not found");
        }
    }

    @Override
    public TopicsResponseDto getAllTopicsDto() {
        List<Topic> topics = topicRepository.findAll();
        return toTopicsResponseDto(topics);
    }

    @Override
    public TopicResponseDto getTopicByIdDto(Integer id) {
        return topicRepository.findById(id)
                .map(topic -> modelMapper.map(topic, TopicResponseDto.class))
                .orElse(null);
    }

    @Override
    public TopicResponseDto createTopicDto(Topic topic) {
        Topic savedTopic = topicRepository.save(topic);
        return modelMapper.map(savedTopic, TopicResponseDto.class);
    }

    @Override
    public TopicResponseDto updateTopicDto(Integer id, Topic topicDetails) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topic.setTitle(topicDetails.getTitle());
                    topic.setDescription(topicDetails.getDescription());
                    Topic updatedTopic = topicRepository.save(topic);
                    return modelMapper.map(updatedTopic, TopicResponseDto.class);
                })
                .orElse(null);
    }

    @Override
    public TopicsResponseDto getSubscribedTopicsDto(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Topic> topics = user.get().getSubscribedTopics();
            return toTopicsResponseDto(topics);
        }
        return new TopicsResponseDto();
    }

    private TopicsResponseDto toTopicsResponseDto(List<Topic> topics) {
        List<TopicResponseDto> topicDtos = topics.stream()
                .map(topic -> modelMapper.map(topic, TopicResponseDto.class))
                .collect(Collectors.toList());

        TopicsResponseDto response = new TopicsResponseDto();
        response.setTopics(topicDtos);
        return response;
    }
}