package com.sparta.todoapp.domain.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("가입 테스트")
    void test1() {
        //given
        String username = "username";
        String password = "password";

        SignupRequestDto signupRequestDto = new SignupRequestDto(username, password);

        UserService userService = new UserServiceImpl(userRepository, passwordEncoder);

        given(userRepository.findByUsername(username)).willReturn(Optional.empty());
        //when
        userService.userSignup(signupRequestDto);
        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

}