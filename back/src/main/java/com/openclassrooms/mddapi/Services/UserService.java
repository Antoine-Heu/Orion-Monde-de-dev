package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Dto.UserResponseDto;
import com.openclassrooms.mddapi.Dto.UserDetailResponseDto;
import com.openclassrooms.mddapi.Dto.UserDto;
import com.openclassrooms.mddapi.Models.User;

public interface UserService {
    User getCurrentUser(String identifier);
    UserResponseDto getCurrentUserToDto(String identifier);
    UserDetailResponseDto getCurrentUserDetails(String identifier);
    UserResponseDto updateCurrentUser(String identifier, UserDto userDto);
}