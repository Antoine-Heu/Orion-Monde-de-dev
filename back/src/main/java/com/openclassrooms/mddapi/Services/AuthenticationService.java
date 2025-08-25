package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Controllers.payloads.LoginRequest;
import com.openclassrooms.mddapi.Dto.TokenResponseDto;
import com.openclassrooms.mddapi.Models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    public TokenResponseDto authenticate(LoginRequest loginRequest) {
        Optional<User> user1 = userRepository.findByUsername(loginRequest.getUsername());
    };
}
