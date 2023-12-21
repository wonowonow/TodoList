package com.sparta.todoapp.domain.card.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.todoapp.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardListResponseDto {

    private String title;
    private String imageUrl;
    private String author;
    private LocalDateTime createdAt;
    private Boolean isDone;

    public CardListResponseDto(Card card) {
        this.title = card.getTitle();
        this.author = card.getUser().getUsername();
        this.createdAt = card.getCreatedAt();
        this.isDone = card.getIsDone();
        this.imageUrl = card.getImageUrl();
    }

    @QueryProjection
    public CardListResponseDto(String title, String author, LocalDateTime createdAt, Boolean isDone,
            String imageUrl) {
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.isDone = isDone;
        this.imageUrl = imageUrl;
    }
}
