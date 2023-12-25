package com.sparta.todoapp.domain.card.repository;

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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
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
    void 카드_저장_테스트() {
        //given
        String title = "제목";
        String content = "내용";
        CardPostRequestDto cardPostRequestDto = CardPostRequestDto
                .builder()
                .title(title)
                .content(content)
                .build();
        Card card = Card.builder().user(user)
                .title(cardPostRequestDto.getTitle())
                .content(cardPostRequestDto.getContent())
                .isDone(false)
                .build();
        // when
        Card savedCard = cardRepository.save(card);
        // then
        Assertions.assertNotNull(savedCard);
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
        Card card = cardRepository.save(
                Card.builder().title(title).content(content).user(user).isDone(false).build());
        // When
        Card savedCard = cardRepository.findById(card.getId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_TODO)
        );
        // Then
        Assertions.assertEquals(title, savedCard.getTitle());
        Assertions.assertEquals(content, savedCard.getContent());
    }
}