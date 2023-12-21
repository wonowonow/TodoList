package com.sparta.todoapp.domain.card.service;

import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.hashtag.service.HashTagService;
import com.sparta.todoapp.domain.s3.S3UploadService;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Primary
public class CardServiceImplV2 implements CardService {

    private final CardRepository cardRepository;
    private final HashTagService hashTagService;
    private final S3UploadService s3UploadService;

    @Override
    public CardResponseDto createTodoCard(CardPostRequestDto cardPostRequestDto, User user) {

        String title = cardPostRequestDto.getTitle();
        String content = cardPostRequestDto.getContent();
        MultipartFile multipartFile = cardPostRequestDto.getFile();

        String imageUrl = null;

        if (multipartFile != null) {
            imageUrl = s3UploadService.saveFile(multipartFile);
        }

        Card card = Card.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .isDone(false)
                .user(user)
                .build();

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

    @Override
    @Transactional
    public CardResponseDto editTodoCard(CardPostRequestDto cardPostRequestDto, Long cardId,
            User user) {

        Card card = getCard(cardId);

        MultipartFile multipartFile = cardPostRequestDto.getFile();

        String imageUrl = null;

        if(multipartFile != null) {
            imageUrl = s3UploadService.saveFile(multipartFile);
        }

        if (card.getUser().getId().equals(user.getId())) {
            card.setContent(cardPostRequestDto.getContent());
            card.setTitle(cardPostRequestDto.getTitle());
            card.setImageUrl(imageUrl);
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_EDIT_ONLY_WRITER);
        }

        hashTagService.deleteTag(cardId);

        List<String> hashTagList = hashTagService.findHashTagByCardContent(card);

        hashTagService.saveTag(hashTagList, card);

        return new CardResponseDto(card);
    }

    @Override
    @Transactional
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

    @Override
    public Page<CardListResponseDto> searchTodoCardWithHashTag(String searchHashTag,
            Pageable pageable) {

        return cardRepository.findCardByHashTagCustom(searchHashTag, pageable);
    }

    private Card getCard(Long cardId) {

        return cardRepository.findById(cardId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_TODO)
        );
    }
}
