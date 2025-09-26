package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Controllers.payloads.LoginRequest;
import com.openclassrooms.mddapi.Controllers.payloads.RegisterRequest;
import com.openclassrooms.mddapi.Dto.TokenResponseDto;
import com.openclassrooms.mddapi.Models.User;
import com.openclassrooms.mddapi.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
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
        try {
            logger.info("Attempting authentication for identifier: {}", loginRequest.getIdentifier());
            User user = userRepository.findByUsernameOrEmail(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                    .orElseThrow(() -> {
                        logger.warn("User not found for identifier: {}", loginRequest.getIdentifier());
                        return new RuntimeException("Invalid identifier");
                    });

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("Invalid password for user: {}", user.getEmail());
                throw new RuntimeException("Invalid password");
            }

            logger.info("Authentication successful for user: {}", user.getEmail());
            String token = jwtService.generateToken(user.getEmail());
            return new TokenResponseDto(token);
        } catch (Exception e) {
            logger.error("Authentication failed for identifier: {} - Error: {}", loginRequest.getIdentifier(), e.getMessage(), e);
            throw e;
        }
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
