package com.sparta.todoapp.domain.user.dto;

import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotEmpty
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;

    public SignupRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .role(UserRoleEnum.USER)
                .build();
    }
}
