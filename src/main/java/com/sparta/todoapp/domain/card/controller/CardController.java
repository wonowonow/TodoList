package com.sparta.todoapp.domain.card.controller;

import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.service.CardService;
import com.sparta.todoapp.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public Map<String, List<CardListResponseDto>> getTodoCards() {
        return cardService.getTodoCards();
    }

    @GetMapping("/todos/{cardId}")
    public CardResponseDto getTodoCard(@PathVariable Long cardId) {
        return cardService.getTodoCard(cardId);
    }

    @PutMapping("/todos/{cardId}")
    public CardResponseDto editTodoCard(@RequestBody @Valid CardPostRequestDto cardPostRequestDto,@PathVariable Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cardService.editTodoCard(cardPostRequestDto, cardId, userDetails.getUser());
    }

    @PatchMapping("/todos/{cardId}")
    public CardResponseDto changeTodoCardDone(@PathVariable Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CardDoneStatusRequestDto cardDoneStatusRequestDto){
        return cardService.changeTodoCardDone(cardId, userDetails.getUser(), cardDoneStatusRequestDto);
    }
}
