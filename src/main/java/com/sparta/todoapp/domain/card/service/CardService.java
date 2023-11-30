package com.sparta.todoapp.domain.card.service;

import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Map<String, List<CardListResponseDto>> getTodoCards() {

        List<Card> cardList = cardRepository.findAllByOrderByCreatedAtDesc();

        return cardList.stream()
                .map(CardListResponseDto::new)
                .collect(Collectors.groupingBy(CardListResponseDto::getAuthor));
    }

    public CardResponseDto getTodoCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 투 두 카드가 존재하지 않습니다."));
        CardResponseDto cardResponseDto = new CardResponseDto(card);
        return cardResponseDto;
    }

    @Transactional
    public CardResponseDto editTodoCard(CardPostRequestDto cardPostRequestDto, Long cardId,
            User user) {
        List<Card> cardList = cardRepository.findAllByUser(user);
        CardResponseDto cardResponseDto = new CardResponseDto();
        for (Card card : cardList) {
            if (card.getId().equals(cardId)) {
                card.setContent(cardPostRequestDto.getContent());
                card.setTitle(cardPostRequestDto.getTitle());
                cardRepository.save(card);
                cardResponseDto = new CardResponseDto(card);
            }
        }
        return cardResponseDto;
    }

    @Transactional
    public CardResponseDto changeTodoCardDone(Long cardId, User user,
            CardDoneStatusRequestDto cardDoneStatusRequestDto) {
        List<Card> cardList = cardRepository.findAllByUser(user);
        CardResponseDto cardResponseDto = new CardResponseDto();
        for (Card card : cardList) {
            if (card.getId().equals(cardId)) {

                card.setIsDone(cardDoneStatusRequestDto.getIsDone());
                cardRepository.save(card);
                cardResponseDto = new CardResponseDto(card);
            }
        }
        return cardResponseDto;
    }
}
