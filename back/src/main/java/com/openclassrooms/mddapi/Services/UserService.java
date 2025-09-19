package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.UserResponseDto;
import com.openclassrooms.mddapi.Dto.UserDetailResponseDto;
import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User getCurrentUser(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponseDto getCurrentUserToDto(String identifier) {
        User user = getCurrentUser(identifier);
        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserDetailResponseDto getCurrentUserDetails(String identifier) {
        User user = getCurrentUser(identifier);
        UserDetailResponseDto userDetails = modelMapper.map(user, UserDetailResponseDto.class);

        if (user.getSubscribedTopics() != null) {
            List<TopicResponseDto> topicDtos = user.getSubscribedTopics().stream()
                    .map(topic -> modelMapper.map(topic, TopicResponseDto.class))
                    .collect(Collectors.toList());
            userDetails.setSubscribedTopics(topicDtos);
        }

        return userDetails;
    }
}