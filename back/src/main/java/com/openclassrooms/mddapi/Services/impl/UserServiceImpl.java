package com.openclassrooms.mddapi.Services.impl;

import com.openclassrooms.mddapi.Dto.TopicResponseDto;
import com.openclassrooms.mddapi.Dto.UserDetailResponseDto;
import com.openclassrooms.mddapi.Dto.UserDto;
import com.openclassrooms.mddapi.Dto.UserResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import com.openclassrooms.mddapi.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getCurrentUser(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserResponseDto getCurrentUserToDto(String identifier) {
        User user = getCurrentUser(identifier);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
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

    @Override
    public UserResponseDto updateCurrentUser(String identifier, UserDto userDto) {
        User user = getCurrentUser(identifier);
        boolean emailChanged = false;

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
            emailChanged = true;
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        UserResponseDto response = modelMapper.map(updatedUser, UserResponseDto.class);

        // Note: Le front devra gérer la déconnexion si l'email a changé
        return response;
    }
}