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
@Slf4j(topic = "유저 서비스")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void userSignup(SignupRequestDto signupRequestDto) {
        log.info("유저 회원가입");
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ExceptionCode.BAD_REQUEST_USERNAME_ALREADY_IN_USE);
        }

        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, role);
        userRepository.save(user);
    }
}
