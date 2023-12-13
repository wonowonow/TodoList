package com.sparta.todoapp.domain.comment.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@DisplayName("댓글 Entity JPA 테스트")
class CommentTest {

    @Autowired
    TestEntityManager entityManager;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        String username = "username";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        UserRoleEnum role = UserRoleEnum.USER;
        User savedUser = entityManager.persistFlushFind(new User(username, encodedPassword, role));

        String cardTitle = "카드제목";
        String cardContent = "카드내용";
        Card savedCard = entityManager.persistFlushFind(
                new Card(cardTitle, cardContent, savedUser));
    }

    @Test
    @DisplayName("댓글 저장 시 저장한 유저와 댓글의 참조키가 일치하고, 올바른 투 두 카드에 저장됐는지 확인")
    void 댓글_저장_성공() {
        // Given
        User user = entityManager.find(User.class, 1L);
        Card card = entityManager.find(Card.class, 1L);
        Long userId = user.getId();
        Long cardId = card.getId();
        String commentContent = "대세는펩시제로";
        // When
        Comment savedComment = entityManager.persistFlushFind(
                new Comment(commentContent, user, card));
        // Then
        Assertions.assertEquals(userId, savedComment.getUser().getId());
        Assertions.assertEquals(cardId, savedComment.getCard().getId());
        Assertions.assertEquals(commentContent, savedComment.getContent());
    }
}