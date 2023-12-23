package com.sparta.todoapp.domain.card_hashtag.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.repository.HashTagRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class CardHashTagRepositoryTest {

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardHashTagRepository cardHashTagRepository;

    private User user;

    private Card card;

    private HashTag hashTag;

    @BeforeEach
    void setUp() {
        user = User.builder().username("username").password("password").role(UserRoleEnum.USER).build();
        user = userRepository.save(user);
        card = Card.builder().title("제목").content("내용#나나").isDone(false).user(user).build();
        card = cardRepository.save(card);
        hashTag = HashTag.builder().name("#나나").build();
        hashTag = hashTagRepository.save(hashTag);
    }

    @Test
    @DisplayName("카드_해시태그 저장 태스트")
    void 카드_해시태그_저장_테스트() {
        // Given
        CardHashTag cardHashTag = new CardHashTag(card, hashTag);
        // When
        CardHashTag savedCardHashTag = cardHashTagRepository.save(cardHashTag);
        // Then
        assertThat(savedCardHashTag).isNotNull();
        assertThat(savedCardHashTag.getCard()).isEqualTo(cardHashTag.getCard());
        assertThat(savedCardHashTag.getHashTag()).isEqualTo(cardHashTag.getHashTag());
    }

    @Test
    @DisplayName("카드_해시태그 삭제 태스트")
    void 카드_해시태그_삭제_테스트() {
        // Given
        CardHashTag cardHashTag = new CardHashTag(card, hashTag);
        CardHashTag savedCardHashTag = cardHashTagRepository.save(cardHashTag);
        // When
        cardHashTagRepository.deleteTagMappingByCardId(savedCardHashTag.getCard().getId());
        // Then
        List<CardHashTag> foundCardHashTag = cardHashTagRepository.findAllByCard_Id(savedCardHashTag.getCard().getId());
        assertThat(foundCardHashTag.size()).isEqualTo(0);
    }
}