package com.sparta.todoapp.domain.comment.dto;

import com.sparta.todoapp.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    String content;
    String Author;
    LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.Author = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
    }
}
