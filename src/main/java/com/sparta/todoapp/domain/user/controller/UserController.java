package com.sparta.todoapp.domain.user.controller;

import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(
            @RequestBody @Valid SignupRequestDto signupRequestDto) {
        userService.userSignup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료 되었습니다.");
    }
}
