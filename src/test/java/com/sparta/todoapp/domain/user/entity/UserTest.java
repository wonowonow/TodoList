package com.sparta.todoapp.domain.user.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@DisplayName("User Entity 테스트")
class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("유저 저장 시 아이디, 비밀번호, role이 일치해야 한다 / 비밀번호는 Encode 해서 저장")
    void 유저_저장_성공() {
        // Given
        String username = "username";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        UserRoleEnum role = UserRoleEnum.USER;
        // When
        User savedUser = entityManager.persistFlushFind(new User(username, encodedPassword, role));
        // Then
        Assertions.assertEquals(username, savedUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        Assertions.assertEquals(role, savedUser.getRole());
    }

    @Test
    @DisplayName("유저 저장 시 username이 null이면 예외가 발생한다")
    void 유저_저장_실패_username_null() {
        // Given
        String username = null;
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        UserRoleEnum role = UserRoleEnum.USER;
        // When & Then
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                entityManager.persist(new User(username, encodedPassword, role))
        );
    }

    @Test
    @DisplayName("유저 저장 시 password가 null이면 예외를 반환한다")
    void 유저_저장_실패_password_null() {
        // Given
        String username = "username";
        String password = null;
        UserRoleEnum role = UserRoleEnum.USER;
        // When & Then
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                entityManager.persist(new User(username, password, role))
        );
    }
}