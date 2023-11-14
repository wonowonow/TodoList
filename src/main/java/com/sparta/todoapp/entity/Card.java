package com.sparta.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_ID")
    private BigInteger id;
    @NotEmpty
    @Column(nullable = false, length = 50)
    private String title;
    @NotEmpty
    @Column(nullable = false, length = 255)
    private String content;
    @OneToMany
    @JoinColumn(name = "COMMENT_ID")
    private Set<Comment> comments;
}
