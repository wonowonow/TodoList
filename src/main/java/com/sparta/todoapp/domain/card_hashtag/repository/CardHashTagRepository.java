package com.sparta.todoapp.domain.card_hashtag.repository;

import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardHashTagRepository extends JpaRepository<CardHashTag, Long>, CardHashTagQueryRepository{

    List<CardHashTag> findAllByCard_Id(Long cardId);

}
