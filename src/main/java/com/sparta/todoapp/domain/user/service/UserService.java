package com.sparta.todoapp.domain.user.service;

import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import com.sparta.todoapp.global.jwt.JwtUtil;
import com.sparta.todoapp.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * @param signupRequestDto 유저 회원 가입 요청 정보
     */
    void userSignup(SignupRequestDto signupRequestDto);
}
