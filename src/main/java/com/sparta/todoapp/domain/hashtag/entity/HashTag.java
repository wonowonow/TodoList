package com.sparta.todoapp.domain.hashtag.entity;

import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@Table(name = "hashtags")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "hashTag")
    private List<CardHashTag> cardHashTags;

    @Builder
    public HashTag(Long id, String name, List<CardHashTag> cardHashTags) {
        this.id = id;
        this.name = name;
        this.cardHashTags = cardHashTags;
    }
}
