package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void userSignup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        User user = new User(username, password);
        userRepository.save(user);
    }
}
