package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Controllers.payloads.LoginRequest;
import com.openclassrooms.mddapi.Controllers.payloads.RegisterRequest;
import com.openclassrooms.mddapi.Dto.TokenResponseDto;
import com.openclassrooms.mddapi.Dto.UserResponseDto;
import com.openclassrooms.mddapi.Services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequest loginRequest) {
        return this.authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/register")
    public TokenResponseDto register(@RequestBody RegisterRequest registerRequest) {
        return this.authenticationService.authenticate(registerRequest);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    public UserResponseDto getMe(Principal principal) {
        return authenticationService.getCurrentUserToDto(principal.getName());
    }
}
