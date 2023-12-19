package com.sparta.todoapp.domain.card_hashtag.entity;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.hashtag.entity.HashTag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "card_hashtags")
public class CardHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    public CardHashTag(Card card, HashTag hashTag) {
        this.card = card;
        this.hashTag = hashTag;
    }
}
