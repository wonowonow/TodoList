package com.sparta.todoapp.dto.card;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CardPostRequestDto {
    @NotEmpty
    String title;
    @NotEmpty
    String content;
}
