package com.sparta.todoapp.domain.card.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardPostRequestDto {
    @NotEmpty
    String title;

    @NotEmpty
    String content;
}
