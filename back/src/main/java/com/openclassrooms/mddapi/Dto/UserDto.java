package com.openclassrooms.mddapi.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String password;

    public UserDto(String name, String email, String password) {
        this.username = name;
        this.email = email;
        this.password = password;
    }
}
