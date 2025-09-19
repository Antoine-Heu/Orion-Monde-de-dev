package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Controllers.payloads.LoginRequest;
import com.openclassrooms.mddapi.Controllers.payloads.RegisterRequest;
import com.openclassrooms.mddapi.Dto.TokenResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthenticationService(
        UserRepository userRepository,
        JWTService jwtService,
        PasswordEncoder passwordEncoder,
        ModelMapper modelMapper
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public TokenResponseDto authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameOrEmail(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                .orElseThrow(() -> new RuntimeException("Invalid identifier"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new TokenResponseDto(token);
    };

    public TokenResponseDto authenticate(RegisterRequest registerRequest) {
        Optional<User> existingUserByUsername = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());
        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("Problem during creation of account");
        }

        User newUser = modelMapper.map(registerRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        this.userRepository.save(newUser);
        String token = jwtService.generateToken(newUser.getEmail());
        return new TokenResponseDto(token);
    }
}
