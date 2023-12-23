package com.sparta.todoapp.domain.card.entity;

import com.sparta.todoapp.domain.card_hashtag.entity.CardHashTag;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 50)
    private String title;

    @NotEmpty
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isDone;

    @Column(length = 500)
    private String imageUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "card")
    private List<Comment> comments;

    @OneToMany(mappedBy = "card")
    private List<CardHashTag> cardHashTags;

    @Builder
    public Card(String title, String content, Boolean isDone, String imageUrl, User user) {
        this.title = title;
        this.content = content;
        this.isDone = isDone;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public static Card createCard(String title, String content, String imageUrl, User user) {

        return Card.builder()
                .title(title)
                .content(content)
                .isDone(false)
                .user(user)
                .imageUrl(imageUrl)
                .build();
    }

    public void editTodoCard(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void changeCardStatus(Boolean isDone) {
        this.isDone = isDone;
    }
}
