package com.sparta.todoapp.domain.card_hashtag.repository;

public interface CardHashTagQueryRepository {
    void deleteTagMappingByCardId(Long cardId);
}
