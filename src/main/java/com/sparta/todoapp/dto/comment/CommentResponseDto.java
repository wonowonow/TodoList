package com.sparta.todoapp.dto.comment;

import com.sparta.todoapp.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
