package com.sparta.todoapp.dto.card;

import com.sparta.todoapp.entity.Card;
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
