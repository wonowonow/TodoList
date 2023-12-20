package com.sparta.todoapp.domain.card_hashtag.repository;

import static com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag.cardHashTag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardHashTagQueryRepositoryImpl implements CardHashTagQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteTagMappingByCardId(Long cardId) {
        queryFactory
                .delete(cardHashTag)
                .where(cardHashTag.card.id.eq(cardId))
                .execute();

    }
}
