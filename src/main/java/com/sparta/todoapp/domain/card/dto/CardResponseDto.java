package com.sparta.todoapp.domain.card.dto;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class CardResponseDto {

    private String title;
    private String content;
    private String imageUrl;
    private String author;
    private Boolean isDone;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;

    public CardResponseDto(Card card) {
        this.title = card.getTitle();
        this.content = card.getContent();
        this.imageUrl = card.getImageUrl();
        this.author = card.getUser().getUsername();
        this.isDone = card.getIsDone();
        this.createdAt = card.getCreatedAt();
        this.comments = card.getComments()
                            .stream()
                            .map(CommentResponseDto::new)
                            .collect(Collectors.toList());
    }

    public CardResponseDto(String title, String content, String imageUrl, String author,
            Boolean isDone) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.author = author;
        this.isDone = isDone;
    }
}
