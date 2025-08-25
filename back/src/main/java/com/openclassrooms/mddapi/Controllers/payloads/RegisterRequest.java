package com.openclassrooms.mddapi.Controllers.payloads;

import com.openclassrooms.mddapi.Validations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String email;

    @ValidPassword
    private String password;
}
