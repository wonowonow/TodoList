package com.sparta.todoapp.domain.card.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CardPostRequestDto {
    @NotEmpty
    String title;

    @NotEmpty
    String content;
}
