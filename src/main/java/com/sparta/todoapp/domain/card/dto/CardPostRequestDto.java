package com.sparta.todoapp.domain.card.dto;

import com.sparta.todoapp.domain.card.entity.Card;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardPostRequestDto {
    @NotEmpty
    String title;

    @NotEmpty
    String content;

    public CardPostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
