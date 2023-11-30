package com.sparta.todoapp.domain.card.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.user.entity.User;
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
}