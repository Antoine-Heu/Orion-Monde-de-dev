package com.openclassrooms.mddapi.Controllers.payloads;

import com.openclassrooms.mddapi.Validations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;

    @ValidPassword
    private String password;
}
