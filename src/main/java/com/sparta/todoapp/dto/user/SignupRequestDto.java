package com.sparta.todoapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotEmpty
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;
}
