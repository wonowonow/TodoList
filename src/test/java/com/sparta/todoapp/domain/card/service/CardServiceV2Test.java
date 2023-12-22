package com.sparta.todoapp.domain.card.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
import com.sparta.todoapp.domain.hashtag.service.HashTagService;
import com.sparta.todoapp.domain.s3.S3UploadService;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.global.exception.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CardServiceV2Test {

    @Mock
    CardRepository cardRepository;

    @Mock
    HashTagService hashTagService;
    
    @Mock
    S3UploadService s3UploadService;


    @Test
    @DisplayName("카드 생성 테스트")
    void test1() {
        // given
        String title = "제목";
        String content = "내용";
        User user = new User();
        CardPostRequestDto cardPostRequestDto = CardPostRequestDto.builder().title(title).content(content).build();

        CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);

        // when
        cardService.createTodoCard(cardPostRequestDto, user);
        // then
        verify(cardRepository, times(1)).save(any(Card.class));
    }


    @Nested
    @DisplayName("카드 불러오기 모음")
    class getCard {

        @Test
        @DisplayName("카드 불러오기 테스트 - 성공")
        void 카드_불러오기_테스트_성공() {
            // given
            CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);

            Long cardId = 1L;
            User user = new User();
            Card card = Card.builder().title("제목").content("내용").user(user).build();

            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            // when
            CardResponseDto result = cardService.getTodoCard(cardId);
            // then
            Assertions.assertEquals(card.getTitle(), result.getTitle());
            Assertions.assertEquals(card.getContent(), result.getContent());
            Assertions.assertEquals(card.getUser().getUsername(), result.getAuthor());
        }

        @Test
        @DisplayName("카드 불러오기 테스트 - 실패")
        void 카드_불러오기_테스트_실패() {
            // given
            Long cardId = 1L;
            CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);

            given(cardRepository.findById(cardId)).willReturn(Optional.empty());
            // when & then
            assertThatThrownBy(() -> cardService.getTodoCard(cardId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 투 두 카드는 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("카드 여러개 불러오기 테스트")
    void test3() {
        // given
        CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);
        User user1 = new User("username1", "password1", UserRoleEnum.USER);
        User user2 = new User("username2", "password2", UserRoleEnum.USER);
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.builder().title("제목 1").content("내용 1").user(user1).build());
        cardList.add(Card.builder().title("제목 2").content("내용 2").user(user2).build());
        cardList.add(Card.builder().title("제목 3").content("내용 3").user(user1).build());
        cardList.add(Card.builder().title("제목 4").content("내용 4").user(user1).build());
        cardList.add(Card.builder().title("제목 5").content("내용 5").user(user1).build());
        cardList.add(Card.builder().title("제목 6").content("내용 6").user(user1).build());
        cardList.add(Card.builder().title("제목 7").content("내용 7").user(user1).build());
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

    @Nested
    class 카드_변경 {

        @Test
        @DisplayName("카드 변경 테스트 - 성공")
        void 카드_변경_테스트_성공() {
            // given
            CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);
            Long cardId = 1L;
            String title = "수정 제목";
            String content = "수정 내용";
            User user = new User("username", "password", UserRoleEnum.USER);
            user.setId(1L);
            Card card = Card.builder().title("제목").content("내용").user(user).build();
            CardPostRequestDto cardPostRequestDto = CardPostRequestDto.builder().title(title).content(content).build();
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            // when
            CardResponseDto result = cardService.editTodoCard(cardPostRequestDto, cardId, user);
            // then
            Assertions.assertEquals(title, result.getTitle());
        }

        @Test
        @DisplayName("카드 변경 테스트 - 실패 (투 두 카드 없음)")
        void 카드_변경_테스트_실패_카드_없음() {
            // given
            CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);
            String title = "수정 제목";
            String content = "수정 내용";
            CardPostRequestDto cardPostRequestDto = CardPostRequestDto.builder().title(title).content(content).build();
            Long cardId = 1L;
            User user = new User("username", "password", UserRoleEnum.USER);
            user.setId(1L);
            // when & then
            assertThatThrownBy(() -> cardService.editTodoCard(cardPostRequestDto, cardId, user))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 투 두 카드는 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카드 변경 테스트 - 실패 (권한 없음)")
        void 카드_변경_테스트_실패_권한_없음() {
            // given
            CardService cardService = new CardServiceImplV2(cardRepository, hashTagService, s3UploadService);
            Long cardId = 1L;
            String title = "수정 제목";
            String content = "수정 내용";
            User user1 = new User("username", "password", UserRoleEnum.USER);
            user1.setId(1L);
            User user2 = new User("username", "password", UserRoleEnum.USER);
            user2.setId(2L);
            Card card = Card.builder().title(title).content(content).user(user1).build();
            CardPostRequestDto cardPostRequestDto = CardPostRequestDto.builder().title(title).content(content).build();
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            // when & then
            assertThatThrownBy(() -> cardService.editTodoCard(cardPostRequestDto, cardId, user2))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("작성자만 수정 할 수 있습니다");
        }
    }

    @Nested
    @DisplayName("카드 상태 변경 테스트")
    class 카드_상태_변경_테스트 {

        @Test
        @DisplayName("카드 상태 변경 테스트 - 성공")
        void 카드_상태_변경_테스트_성공() {
            // given
            CardService cardService = new CardServiceImplV1(cardRepository);
            User user = new User("username", "password", UserRoleEnum.USER);
            user.setId(1L);
            Card card = Card.builder().title("제목").content("내용").user(user).build();
            card.setId(1L);
            CardResponseDto cardResponseDto = new CardResponseDto(card);
            CardDoneStatusRequestDto doneStatusRequestDto = new CardDoneStatusRequestDto();
            doneStatusRequestDto.setIsDone(true);
            given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
            // when
            CardResponseDto result = cardService.changeTodoCardDone(card.getId(), user,
                    doneStatusRequestDto);
            // then
            Assertions.assertNotNull(result);
            Assertions.assertEquals(cardResponseDto.getTitle(), result.getTitle());
            Assertions.assertEquals(cardResponseDto.getContent(), result.getContent());
            Assertions.assertTrue(result.getIsDone());
        }

        @Test
        @DisplayName("카드 상태 변경 테스트 - 실패 (투 두 카드 없음)")
        void 카드_상태_변경_테스트_실패_카드_없음() {
            // given
            CardService cardService = new CardServiceImplV1(cardRepository);
            User user = new User("username", "password", UserRoleEnum.USER);
            user.setId(1L);
            Card card = Card.builder().title("제목").content("내용").user(user).build();
            card.setId(1L);
            CardResponseDto cardResponseDto = new CardResponseDto(card);
            CardDoneStatusRequestDto doneStatusRequestDto = new CardDoneStatusRequestDto();
            doneStatusRequestDto.setIsDone(true);
            given(cardRepository.findById(card.getId())).willReturn(Optional.empty());
            // when & then
            assertThatThrownBy(
                    () -> cardService.changeTodoCardDone(card.getId(), user, doneStatusRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 투 두 카드는 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카드 상태 변경 테스트 - 실패 (권한 없음)")
        void 카드_상태_변경_테스트_실패_권한_없음() {
            // given
            CardService cardService = new CardServiceImplV1(cardRepository);
            User user1 = new User("username", "password", UserRoleEnum.USER);
            user1.setId(1L);
            User user2 = new User("username", "password", UserRoleEnum.USER);
            user2.setId(2L);
            Card card = Card.builder().title("제목").content("내용").user(user1).build();
            card.setId(1L);
            CardResponseDto cardResponseDto = new CardResponseDto(card);
            CardDoneStatusRequestDto doneStatusRequestDto = new CardDoneStatusRequestDto();
            doneStatusRequestDto.setIsDone(true);
            given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
            // when & then
            assertThatThrownBy(
                    () -> cardService.changeTodoCardDone(card.getId(), user2, doneStatusRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("작성자만 수정 할 수 있습니다");
        }
    }

    @Nested
    class 카드_검색_테스트_모음 {

        @Test
        @DisplayName("해시태그 기준 검색")
        void 해시태그() {
            // Given
            CardService cardService = new CardServiceImplV1(cardRepository);

            String title = "제목";
            String content = "앞#내용 뒤 띄어쓰기";

            User user = User.builder()
                    .username("username")
                    .password("password")
                    .role(UserRoleEnum.USER)
                    .build();

            Pageable pageable = PageRequest.of(0, 1);
            Card card = Card.builder().title(title).content(content).user(user).build();
            Page<CardListResponseDto> result = new PageImpl<>(List.of(new CardListResponseDto(card)));

            given(cardService.searchTodoCardWithHashTag("#내용", pageable)).willReturn(result);
            // When
            Page<CardListResponseDto> searchedTodoCard = cardService.searchTodoCardWithHashTag("#내용", pageable);
            // Then
            Assertions.assertNotNull(searchedTodoCard);
            Assertions.assertEquals(result, searchedTodoCard);
        }
    }
}