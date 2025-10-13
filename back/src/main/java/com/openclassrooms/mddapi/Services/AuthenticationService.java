package com.openclassrooms.mddapi.Services;

import com.openclassrooms.mddapi.Controllers.payloads.LoginRequest;
import com.openclassrooms.mddapi.Controllers.payloads.RegisterRequest;
import com.openclassrooms.mddapi.Dto.TokenResponseDto;

public interface AuthenticationService {

    TokenResponseDto authenticate(LoginRequest loginRequest);
    TokenResponseDto authenticate(RegisterRequest registerRequest);
}
