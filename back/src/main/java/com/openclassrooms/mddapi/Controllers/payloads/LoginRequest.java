package com.openclassrooms.mddapi.Controllers.payloads;

import com.openclassrooms.mddapi.Validations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String identifier;

    @ValidPassword
    private String password;
}
