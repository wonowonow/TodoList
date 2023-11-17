package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardResponseDto {
    String title;
    String content;
    String author;
    LocalDateTime createdAt;

    public CardResponseDto(Card card) {
        this.title = card.getTitle();
        this.content = card.getContent();
        this.author = card.getUser().getUsername();
        this.createdAt = card.getCreatedAt();
    }
}
