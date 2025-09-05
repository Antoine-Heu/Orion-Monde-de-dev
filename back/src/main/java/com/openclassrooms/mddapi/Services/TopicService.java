package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.TopicDto;
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

    private TopicDto toTopicDto(Topic topic) {
        return new TopicDto(topic.getId(), topic.getTitle(), topic.getDescription());
    }

    private TopicsResponseDto toTopicsResponseDto(List<Topic> topics) {
        List<TopicDto> topicDtos = topics.stream()
                .map(this::toTopicDto)
                .collect(Collectors.toList());

        TopicsResponseDto response = new TopicsResponseDto();
        response.setTopics(topicDtos);
        return response;
    }

    public TopicsResponseDto getAllTopicsDto() {
        List<Topic> topics = topicRepository.findAll();
        return toTopicsResponseDto(topics);
    }

    public TopicDto getTopicByIdDto(Integer id) {
        return topicRepository.findById(id)
                .map(this::toTopicDto)
                .orElse(null);
    }

    public TopicDto createTopicDto(Topic topic) {
        Topic savedTopic = topicRepository.save(topic);
        return toTopicDto(savedTopic);
    }

    public TopicDto updateTopicDto(Integer id, Topic topicDetails) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topic.setTitle(topicDetails.getTitle());
                    topic.setDescription(topicDetails.getDescription());
                    Topic updatedTopic = topicRepository.save(topic);
                    return toTopicDto(updatedTopic);
                })
                .orElse(null);
    }

    public TopicsResponseDto getSubscribedTopicsDto(Integer userId) {
        List<Topic> topics = topicRepository.findTopicsBySubscriberId(userId);
        return toTopicsResponseDto(topics);
    }
}