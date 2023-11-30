package com.sparta.todoapp.domain.card.entity;

import com.sparta.todoapp.domain.card.dto.CardResponseDto;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "card")
    private List<Comment> comments;

    public Card(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.isDone = false;
    }

    public Card(CardResponseDto cardResponseDto) {
        this.title = cardResponseDto.getTitle();
        this.content = cardResponseDto.getContent();
    }
}
