package com.sparta.todoapp.domain.card.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@DisplayName("Card Entity 테스트 코드")
class CardTest {

    @Autowired
    private TestEntityManager entityManager;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("username", "password", UserRoleEnum.USER);
        entityManager.persist(user);
    }

    @Test
    @DisplayName("투 두 카드 저장 시 제목, 내용, 유저가 일치해야 한다")
    void 카드_저장_테스트(){
        //given
        String title = "카드제목";
        String content = "카드내용";
        Card card = new Card(title, content, user);
        // when
        Card savedCard = entityManager.persistFlushFind(card);
        // then
        Assertions.assertEquals(user, savedCard.getUser());
        Assertions.assertEquals(user.getUsername(), savedCard.getUser().getUsername());
        Assertions.assertEquals(title, savedCard.getTitle());
        Assertions.assertEquals(content, savedCard.getContent());
        Assertions.assertEquals(false, savedCard.getIsDone());
    }

    @Test
    @DisplayName("투 두 카드 저장 시 제목이 null이면 예외를 반환한다")
    void 카드_저장_테스트_제목_Null(){
        //given
        String title = null;
        String content = "카드내용";
        Card card = new Card(title, content, user);
        // when & then
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                entityManager.persist(card)
        );
    }

    @Test
    @DisplayName("투 두 카드 저장 시 내용이 null이면 예외를 반환한다")
    void 카드_저장_테스트_내용_Null(){
        //given
        String title = "카드제목";
        String content = null;
        Card card = new Card(title, content, user);
        // when & then
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                entityManager.persist(card)
        );
    }

}