package com.sparta.todoapp.domain.card.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Test
    @DisplayName("카드 여러개 불러오기 테스트")
    void test3() {
        // given
        CardService cardService = new CardService(cardRepository);
        User user1 = new User("username1","password1", UserRoleEnum.USER);
        User user2 = new User("username2", "password2", UserRoleEnum.USER);
        List<Card> cardList = new ArrayList<>();
        cardList.add(new Card("제목 1", "내용 1", user1));
        cardList.add(new Card("제목 2", "내용 2", user2));
        cardList.add(new Card("제목 3", "내용 3", user1));
        cardList.add(new Card("제목 4", "내용 4", user1));
        cardList.add(new Card("제목 5", "내용 5", user1));
        cardList.add(new Card("제목 6", "내용 6", user1));
        cardList.add(new Card("제목 7", "내용 7", user1));
        given(cardRepository.findAllByOrderByCreatedAtDesc()).willReturn(cardList);
        // when
        Map<String, List<CardListResponseDto>> result = cardService.getTodoCards();
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(user2.getUsername()).size());
        Assertions.assertEquals(6, result.get(user1.getUsername()).size());
        Assertions.assertEquals("제목 3", result.get(user1.getUsername()).get(1).getTitle());
    }

    @Test
    @DisplayName("카드 변경 테스트")
    void test4() {
        // given
        CardService cardService = new CardService(cardRepository);
        CardPostRequestDto cardPostRequestDto = new CardPostRequestDto();
        Long cardId = 1L;
        String title = "수정 제목";
        String content = "수정 내용";
        User user = new User("username", "password", UserRoleEnum.USER);
        user.setId(1L);
        Card card = new Card("제목", "내용", user);
        cardPostRequestDto.setTitle(title);
        cardPostRequestDto.setContent(content);
        given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
        // when
        CardResponseDto result = cardService.editTodoCard(cardPostRequestDto, cardId, user);
        // then
        Assertions.assertEquals(title, result.getTitle());
    }

    @Test
    @DisplayName("카드 상태 변경 테스트")
    void test5() {
        // given
        CardService cardService = new CardService(cardRepository);
        User user = new User("username", "password", UserRoleEnum.USER);
        user.setId(1L);
        Card card = new Card("제목", "내용", user);
        card.setId(1L);
        CardResponseDto cardResponseDto = new CardResponseDto(card);
        CardDoneStatusRequestDto doneStatusRequestDto = new CardDoneStatusRequestDto();
        doneStatusRequestDto.setIsDone(true);
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
        // when
        CardResponseDto result = cardService.changeTodoCardDone(card.getId(), user, doneStatusRequestDto);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(cardResponseDto.getTitle(), result.getTitle());
        Assertions.assertEquals(cardResponseDto.getContent(), result.getContent());
        Assertions.assertTrue(result.getIsDone());
    }
}