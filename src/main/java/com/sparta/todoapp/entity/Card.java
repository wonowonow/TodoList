package com.sparta.todoapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_ID")
    private Long id;
    @NotEmpty
    @Column(nullable = false, length = 50)
    private String title;
    @NotEmpty
    @Column(nullable = false, length = 255)
    private String content;
    @Column(nullable = false)
    private Boolean isDone = false;
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    @OneToMany
    @JoinColumn(name = "COMMENT_ID")
    private Set<Comment> comments;

    public Card(String title, String content) {
        this.title = title;
        this.content = content;
        this.isDone = false;
    }
}
