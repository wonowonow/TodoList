package com.sparta.todoapp.domain.user.service;

import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * @param signupRequestDto 유저 회원 가입 요청 정보
     */
    void userSignup(SignupRequestDto signupRequestDto);
}
