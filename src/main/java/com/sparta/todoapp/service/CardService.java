package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.dto.card.CardPostRequestDto;
import com.sparta.todoapp.dto.card.CardListResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void createTodoCard(CardPostRequestDto cardPostRequestDto, User user) {

        String title = cardPostRequestDto.getTitle();
        String content = cardPostRequestDto.getContent();
        Card card = new Card(title, content, user);
        cardRepository.save(card);
    }

    public List<CardListResponseDto> getTodoCards(User user) {

        List<Card> cardList = cardRepository.findAllByUser(user);
        List<CardListResponseDto> cardListResponseDtoList = new ArrayList<>();

        for(Card card : cardList) {
            cardListResponseDtoList.add(new CardListResponseDto(card));
        }

        return cardListResponseDtoList;
    }

    public CardResponseDto getTodoCard(Long cardId, User user) {
        List<Card> cardList = cardRepository.findAllByUser(user);
        CardResponseDto cardResponseDto = new CardResponseDto();
        for (Card card : cardList) {
            if (card.getId().equals(cardId)) {
                cardResponseDto = new CardResponseDto(card);
            }
        }
        return cardResponseDto;
    }
}
