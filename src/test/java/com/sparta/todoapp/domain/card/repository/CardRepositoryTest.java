package com.sparta.todoapp.domain.card.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.repository.UserRepository;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("username", "password", UserRoleEnum.USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("카드 저장 테스트")
    void 카드_저장_테스트(){
        //given
        String title = "제목";
        String content = "내용";
        CardPostRequestDto cardPostRequestDto = new CardPostRequestDto(title, content);
        Card card = new Card(cardPostRequestDto.getTitle(), cardPostRequestDto.getContent(), user);
        // when
        Card savedCard = cardRepository.save(card);
        // then
        Assertions.assertNotNull(savedCard);
        Assertions.assertEquals(user.getId(), savedCard.getUser().getId());
        Assertions.assertEquals(title, savedCard.getTitle());
        Assertions.assertEquals(content, savedCard.getContent());
        Assertions.assertFalse(savedCard.getIsDone());
    }

    @Test
    @DisplayName("카드 단 건 불러오기 테스트")
    void 카드_단_건_불러오기_테스트() {
        // Given
        String title = "제목";
        String content = "내용";
        cardRepository.save(new Card(title, content, user));
        // When
        Card savedCard = cardRepository.findById(1L).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_TODO)
        );
        // Then
        Assertions.assertEquals(title, savedCard.getTitle());
        Assertions.assertEquals(content, savedCard.getContent());
    }
}