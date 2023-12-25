package com.sparta.todoapp.domain.card.repository;

import static com.sparta.todoapp.domain.card.entity.QCard.card;
import static com.sparta.todoapp.domain.card_hashtag.entity.QCardHashTag.cardHashTag;
import static com.sparta.todoapp.domain.hashtag.entity.QHashTag.hashTag;
import static com.sparta.todoapp.domain.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.QCardListResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CardQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CardListResponseDto> findCardByHashTagCustom(String searchHashTag, Pageable pageable) {

        List<CardListResponseDto> content = queryFactory
                .select(new QCardListResponseDto(
                        card.title,
                        card.user.username.as("author"),
                        card.createdAt,
                        card.isDone,
                        card.imageUrl
                ))
                .from(cardHashTag)
                .join(cardHashTag.hashTag, hashTag)
                .join(cardHashTag.card, card)
                .join(card.user, user)
                .where(hashTag.name.eq(searchHashTag))
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory
                .select(cardHashTag.count())
                .from(cardHashTag)
                .join(cardHashTag.hashTag, hashTag)
                .where(hashTag.name.eq(searchHashTag));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder<>(Card.class, "card");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
