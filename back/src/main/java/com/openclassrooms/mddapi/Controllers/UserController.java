package com.openclassrooms.mddapi.Controllers;

import com.openclassrooms.mddapi.Dto.UserResponseDto;
import com.openclassrooms.mddapi.Dto.UserDetailResponseDto;
import com.openclassrooms.mddapi.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    public UserResponseDto getMe(Principal principal) {
        return userService.getCurrentUserToDto(principal.getName());
    }

    @GetMapping("/me/details")
    @Operation(summary = "Get current authenticated user with full details", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDetailResponseDto getMeDetails(Principal principal) {
        return userService.getCurrentUserDetails(principal.getName());
    }
}