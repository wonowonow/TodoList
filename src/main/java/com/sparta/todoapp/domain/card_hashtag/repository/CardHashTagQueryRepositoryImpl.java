package com.sparta.todoapp.domain.card_hashtag.repository;

import static com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag.cardHashTag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;


public class CardHashTagQueryRepositoryImpl implements CardHashTagQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public CardHashTagQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void deleteTagMappingByCardId(Long cardId) {

        queryFactory
                .delete(cardHashTag)
                .where(cardHashTag.card.id.eq(cardId))
                .execute();
        em.flush();
        em.clear();
    }
}
