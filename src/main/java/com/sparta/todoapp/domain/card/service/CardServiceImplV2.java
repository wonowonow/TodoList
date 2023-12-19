package com.sparta.todoapp.domain.card.service;

import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.hashtag.service.HashTagService;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class CardServiceImplV2 implements CardService {

    private final CardRepository cardRepository;
    private final HashTagService hashTagService;

    @Override
    public CardResponseDto createTodoCard(CardPostRequestDto cardPostRequestDto, User user) {

        String title = cardPostRequestDto.getTitle();
        String content = cardPostRequestDto.getContent();

        // 만약 글 내용이
        // 오늘 할 일 1. 2. 3.
        // #화이팅 #아아 #으으으으 뒤에 이런 글이 더 있다면
        // hashTags[0] = 오늘 할 일 1. 2. 3.
        // hashTags[1] = #화이팅
        // hashTags[2] = #아아
        // hashTags[3] = #으으으으 뒤에 이런 글이 더 있다면
        // 인데?
        // 이걸 한 번 더 나눠서
        // 정규 표현식 사용?
        // #[^\s#]+ 해시 태그 모두 포함

        Card card = new Card(title, content, user);

        cardRepository.save(card);

        List<String> hashTagList = hashTagService.findHashTagByCardContent(card);

        hashTagService.saveTag(hashTagList, card);

        return new CardResponseDto(card);
    }

    @Override
    public Map<String, List<CardListResponseDto>> getTodoCards() {
        List<Card> cardList = cardRepository.findAllByOrderByCreatedAtDesc();

        return cardList.stream()
                .map(CardListResponseDto::new)
                .collect(Collectors.groupingBy(CardListResponseDto::getAuthor));
    }

    @Override
    public CardResponseDto getTodoCard(Long cardId) {
        Card card = getCard(cardId);
        return new CardResponseDto(card);
    }

    @Override // TODO 수정 시 지워진 해시태그 지우기...
    public CardResponseDto editTodoCard(CardPostRequestDto cardPostRequestDto, Long cardId,
            User user) {
        Card card = getCard(cardId);

        if (card.getUser().getId().equals(user.getId())) {
            card.setContent(cardPostRequestDto.getContent());
            card.setTitle(cardPostRequestDto.getTitle());
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_EDIT_ONLY_WRITER);
        }

        List<String> hashTagList = hashTagService.findHashTagByCardContent(card);

        hashTagService.saveTag(hashTagList, card);

        return new CardResponseDto(card);
    }

    @Override
    public CardResponseDto changeTodoCardDone(Long cardId, User user,
            CardDoneStatusRequestDto cardDoneStatusRequestDto) {
        Card card = getCard(cardId);

        if (card.getUser().getId().equals(user.getId())) {
            card.setIsDone(cardDoneStatusRequestDto.getIsDone());
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_EDIT_ONLY_WRITER);
        }

        return new CardResponseDto(card);
    }

    private Card getCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_TODO)
        );
    }
}
