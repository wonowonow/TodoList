package com.sparta.todoapp.domain.hashtag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import com.sparta.todoapp.domain.card_hashtag.repository.CardHashTagRepository;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.repository.HashTagRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class HashTagServiceV1Test {

    @Mock
    HashTagRepository hashTagRepository;

    @Mock
    CardHashTagRepository cardHashTagRepository;

    HashTagService hashTagService;

    User user = null;

    @BeforeEach
    void setUp() {
        hashTagService = new HashTagServiceV1(hashTagRepository, cardHashTagRepository);
        user = User.builder()
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .build();
    }

    @Test
    @DisplayName("투 두 카드 저장 시 카드 내용에서 해시태그를 찾아 반환한다.")
    void searchHashTagInContent() {
        // Given
        String cardTitle = "카드 제목";
        String cardContent = "카드 #저장#테스트 #테테테";

        Card card = new Card(cardTitle, cardContent, user);

        // When
        List<String> result = hashTagService.findHashTagByCardContent(card);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.contains("#저장")).isTrue();
        assertThat(result.contains("#테스트")).isTrue();
        assertThat(result.contains("#테테테")).isTrue();
    }

    @Test
    @DisplayName("해시태그 저장 일반 테스트")
    void test2() {
        // Given
        String cardTitle = "카드 제목";
        String cardContent = "카드 #내용 #하하 #재밌당";
        List<String> hashTagList = new ArrayList<>();
        hashTagList.add("#내용");
        hashTagList.add("#하하");
        hashTagList.add("#재밌당");

        Card card = new Card(cardTitle, cardContent, user);

        when(hashTagRepository
                .existsHashTagByName(any(String.class)))
                .thenReturn(false);
        when(hashTagRepository
                .findHashTagByName(any(String.class)))
                .thenReturn(new HashTag());

        // When
        hashTagService.saveTag(hashTagList, card);

        //Then
        verify(hashTagRepository, times(3))
                .existsHashTagByName(any(String.class));
        verify(hashTagRepository, times(3))
                .save(any(HashTag.class));
        verify(cardHashTagRepository, times(3))
                .save(any(CardHashTag.class));
    }

    @Test
    @DisplayName("해시태그 삭제")
    void 해시태그_삭제_테스트() {
        // Given
        Long cardId = 1L;
        // When
        hashTagService.deleteTag(cardId);
        // Then
        verify(cardHashTagRepository, times(1))
                .deleteTagMappingByCardId(any(Long.class));
    }

}