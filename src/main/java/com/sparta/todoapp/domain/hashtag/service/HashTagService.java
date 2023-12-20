package com.sparta.todoapp.domain.hashtag.service;

import com.sparta.todoapp.domain.card.entity.Card;
import java.util.List;

public interface HashTagService {

    /**
     * 카드 글 내용에서 해시태그를 추출합니다.
     * @param card 저장 직전 투 두 카드
     */
    List<String> findHashTagByCardContent(Card card);

    /**
     * 해시태그 테이블에 해당 해시태그 DB에 존재 하지 않다면 저장하고
     * 존재하지 않다면 Card_HashTag 테이블에 카드 및 해시태그 아이디 저장
     * @param hashTagList
     * @param card
     */
    void saveTag(List<String> hashTagList, Card card);

    /**
     * 카드_해시태그 매핑 테이블에 매개변수로 받은 카드 아이디 삭제
     * 해시태크 테이블은 미삭제
     * @param cardId
     */
    void deleteTag(Long cardId);
}
