package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.card.CardPostRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void createTodoCard(CardPostRequestDto cardPostRequestDto) {
        String title = cardPostRequestDto.getTitle();
        String content = cardPostRequestDto.getContent();

        Card card = new Card(title, content);

        cardRepository.save(card);
    }
}
