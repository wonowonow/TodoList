package com.sparta.todoapp.domain.card.repository;

import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import java.util.List;

public interface CardQueryRepository {

    List<CardListResponseDto> findCardByHashTagCustom(String searchHashTag);
}
