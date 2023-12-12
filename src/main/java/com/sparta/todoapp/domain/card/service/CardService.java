package com.sparta.todoapp.domain.card.service;

import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardListResponseDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.user.entity.User;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    /**
     * @param cardPostRequestDto 투 두 카드 생성 요청 정보
     * @param user               투 두 카드 생성 요청자
     * @return                   투 두 카드 생성 결과
     */
    CardResponseDto createTodoCard(CardPostRequestDto cardPostRequestDto, User user);

    /**
     * @return                   유저별 투두 카드 목록
     */
    Map<String, List<CardListResponseDto>> getTodoCards();

    /**
     * @param cardId            투 두 카드 고유 번호
     * @return                  고유 번호에 해당 하는 투 두 카드
     */
    CardResponseDto getTodoCard(Long cardId);

    /**
     * @param cardPostRequestDto 투 두 카드 수정 요청 정보
     * @param cardId             투 두 카드 고유 번호
     * @param user               투 두 카드 수정 요청자
     * @return                   수정된 투 두 카드
     */
    CardResponseDto editTodoCard(CardPostRequestDto cardPostRequestDto, Long cardId,
            User user);

    /**
     * @param cardId                   투 두 카드 고유 번호
     * @param user                     투 두 카드 상태 변경 요청자
     * @param cardDoneStatusRequestDto 투 두 카드 상태 변경 요청 정보
     * @return                         수정된 투 두 카드
     */
    CardResponseDto changeTodoCardDone(Long cardId, User user,
            CardDoneStatusRequestDto cardDoneStatusRequestDto);
}
