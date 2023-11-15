package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.card.CardPostRequestDto;
import com.sparta.todoapp.service.CardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/todos")
    public CardPostRequestDto createTodoCard (@RequestBody @Valid CardPostRequestDto cardPostRequestDto) {
        cardService.createTodoCard(cardPostRequestDto);
        return cardPostRequestDto;
    }
}
