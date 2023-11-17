package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.dto.card.CardPostRequestDto;
import com.sparta.todoapp.dto.card.CardListResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public CardPostRequestDto createTodoCard(
            @RequestBody @Valid CardPostRequestDto cardPostRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.createTodoCard(cardPostRequestDto, userDetails.getUser());
        return cardPostRequestDto;
    }

    @GetMapping("/todos")
    public List<CardListResponseDto> getTodoCards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cardService.getTodoCards(userDetails.getUser());
    }

    @GetMapping("/todos/{cardId}")
    public CardResponseDto getTodoCard(@PathVariable Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cardService.getTodoCard(cardId, userDetails.getUser());
    }
}
