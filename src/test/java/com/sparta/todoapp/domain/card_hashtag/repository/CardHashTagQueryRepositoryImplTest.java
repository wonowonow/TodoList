package com.sparta.todoapp.domain.card_hashtag.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.repository.HashTagRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.repository.UserRepository;
import com.sparta.todoapp.global.config.JpaConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class CardHashTagQueryRepositoryImplTest {

    @Autowired
    CardHashTagRepository cardHashTagRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void test() {
        User user = User.builder().username("username").password("password").role(UserRoleEnum.USER).build();
        User savedUser = userRepository.save(user);
        Card card = new Card("제목", "내용#나나", savedUser);
        HashTag hashTag = HashTag.builder().name("#나나").build();
        HashTag savedHashTag = hashTagRepository.save(hashTag);
        Card savedCard = cardRepository.save(card);
        CardHashTag cardHashTag = new CardHashTag(savedCard,savedHashTag);
        CardHashTag savedCardHashTag = cardHashTagRepository.save(cardHashTag);
        cardHashTagRepository.findById(savedCardHashTag.getId());

        System.out.println("--------------------------------------");
        cardHashTagRepository.deleteTagMappingByCardId(savedCard.getId());
        System.out.println("------벌크연산 이후-------");
        cardHashTagRepository.findById(cardHashTag.getId());
    }

}