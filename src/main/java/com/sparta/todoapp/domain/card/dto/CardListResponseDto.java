package com.sparta.todoapp.domain.card.dto;

import com.sparta.todoapp.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardListResponseDto {
    String title;
    String author;
    LocalDateTime createdAt;
    Boolean isDone;

    public CardListResponseDto(Card card) {
        this.title = card.getTitle();
        this.author = card.getUser().getUsername();
        this.createdAt = card.getCreatedAt();
        this.isDone = card.getIsDone();
    }
}
