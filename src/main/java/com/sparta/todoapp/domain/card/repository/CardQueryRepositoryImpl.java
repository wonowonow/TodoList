package com.sparta.todoapp.domain.card.repository;

import static com.sparta.todoapp.domain.card.entity.QCard.*;
import static com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag.*;
import static com.sparta.todoapp.domain.hashtag.entity.QHashTag.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.QCardListResponseDto;
import com.sparta.todoapp.domain.card.entity.QCard;
import com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.entity.QHashTag;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CardListResponseDto> findCardByHashTagCustom(String searchHashTag) {

        return queryFactory
                .select(new QCardListResponseDto(
                        card.title,
                        card.user.username.as("author"),
                        card.createdAt,
                        card.isDone))
                .from(cardHashTag)
                .join(cardHashTag.hashTag, hashTag)
                .join(cardHashTag.card, card)
                .where(hashTag.name.eq(searchHashTag))
                .fetch();
    }
}
