package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Dto.TopicsResponseDto;
import com.openclassrooms.mddapi.Models.Topic;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.TopicRepository;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public TopicService(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public void deleteTopic(Integer id) {
        topicRepository.deleteById(id);
    }

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

    private TopicResponseDto toTopicResponseDto(Topic topic) {
        TopicResponseDto dto = new TopicResponseDto();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setDescription(topic.getDescription());
        return dto;
    }

    private TopicsResponseDto toTopicsResponseDto(List<Topic> topics) {
        List<TopicResponseDto> topicDtos = topics.stream()
                .map(this::toTopicResponseDto)
                .collect(Collectors.toList());

        TopicsResponseDto response = new TopicsResponseDto();
        response.setTopics(topicDtos);
        return response;
    }

    public TopicsResponseDto getAllTopicsDto() {
        List<Topic> topics = topicRepository.findAll();
        return toTopicsResponseDto(topics);
    }

    public TopicResponseDto getTopicByIdDto(Integer id) {
        return topicRepository.findById(id)
                .map(this::toTopicResponseDto)
                .orElse(null);
    }

    public TopicResponseDto createTopicDto(Topic topic) {
        Topic savedTopic = topicRepository.save(topic);
        return toTopicResponseDto(savedTopic);
    }

    public TopicResponseDto updateTopicDto(Integer id, Topic topicDetails) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topic.setTitle(topicDetails.getTitle());
                    topic.setDescription(topicDetails.getDescription());
                    Topic updatedTopic = topicRepository.save(topic);
                    return toTopicResponseDto(updatedTopic);
                })
                .orElse(null);
    }

    public TopicsResponseDto getSubscribedTopicsDto(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Topic> topics = user.get().getSubscribedTopics();
            return toTopicsResponseDto(topics);
        }
        return new TopicsResponseDto();
    }
}