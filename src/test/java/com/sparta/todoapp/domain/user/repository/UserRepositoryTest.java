package com.sparta.todoapp.domain.user.repository;

import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 가입 시 저장이 잘 되는지 확인")
    void saveUser() {
        // Given
        String username = "username";
        String password = "password";
        User user = new SignupRequestDto(username, password).toEntity();
        // When
        User savedUser = userRepository.save(user);
        // Then
        Assertions.assertThat(userRepository.count()).isEqualTo(1);
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(username);
        Assertions.assertThat(savedUser.getRole()).isEqualTo(UserRoleEnum.USER);
    }
}