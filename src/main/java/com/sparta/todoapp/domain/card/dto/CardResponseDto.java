package com.sparta.todoapp.domain.card.dto;

import com.sparta.todoapp.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CardResponseDto {
    String title;
    String content;
    String author;
    Boolean isDone;
    LocalDateTime createdAt;

    public CardResponseDto(Card card) {
        this.title = card.getTitle();
        this.content = card.getContent();
        this.author = card.getUser().getUsername();
        this.isDone = card.getIsDone();
        this.createdAt = card.getCreatedAt();
    }
}
