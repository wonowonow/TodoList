package com.sparta.todoapp.domain.card.repository;

import static com.sparta.todoapp.domain.card.entity.QCard.*;
import static com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag.*;
import static com.sparta.todoapp.domain.hashtag.entity.QHashTag.*;
import static com.sparta.todoapp.domain.user.entity.QUser.*;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.QCardListResponseDto;
import com.sparta.todoapp.domain.card.entity.QCard;
import com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import com.sparta.todoapp.domain.hashtag.entity.QHashTag;
import com.sparta.todoapp.domain.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CardListResponseDto> findCardByHashTagCustom(String searchHashTag, Pageable pageable) {

        List<CardListResponseDto> content = queryFactory
                .select(new QCardListResponseDto(
                        card.title,
                        card.user.username.as("author"),
                        card.createdAt,
                        card.isDone))
                .from(cardHashTag)
                .join(cardHashTag.hashTag, hashTag)
                .join(cardHashTag.card, card)
                .join(card.user, user)
                .where(hashTag.name.eq(searchHashTag))
                .orderBy(card.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory
                .select(cardHashTag.count())
                .from(cardHashTag)
                .join(cardHashTag.hashTag, hashTag)
                .join(cardHashTag.card, card)
                .join(card.user, user)
                .where(hashTag.name.eq(searchHashTag));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }
}
