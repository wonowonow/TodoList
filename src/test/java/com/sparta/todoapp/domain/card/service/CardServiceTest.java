package com.sparta.todoapp.domain.card.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    CardRepository cardRepository;


    @Test
    @DisplayName("카드 생성 테스트")
    void test1() {
        // given
        String title = "제목";
        String content = "내용";
        User user = new User();
        CardPostRequestDto cardPostRequestDto = new CardPostRequestDto();

        cardPostRequestDto.setTitle(title);
        cardPostRequestDto.setContent(content);

        CardService cardService = new CardService(cardRepository);

        // when
        cardService.createTodoCard(cardPostRequestDto, user);
        // then
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    @DisplayName("카드 불러오기 테스트")
    void test2() {
        // given
        Long cardId = 1L;
        CardService cardService = new CardService(cardRepository);
        User user = new User();
        Card card = new Card("제목", "내용", user);

        given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
        // when
        CardResponseDto result = cardService.getTodoCard(cardId);
        // then
        Assertions.assertEquals(card.getTitle(), result.getTitle());
        Assertions.assertEquals(card.getContent(), result.getContent());
        Assertions.assertEquals(card.getUser().getUsername(), result.getAuthor());
    }
}