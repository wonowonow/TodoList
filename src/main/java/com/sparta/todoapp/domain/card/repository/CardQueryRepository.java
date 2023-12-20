package com.sparta.todoapp.domain.card.repository;

import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardQueryRepository {

    Page<CardListResponseDto> findCardByHashTagCustom(String searchHashTag, Pageable pageable);
}
