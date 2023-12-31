package com.sparta.todoapp.domain.comment.controller;

import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.comment.service.CommentService;
import com.sparta.todoapp.global.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/todos/{cardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardId,
            @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        return ResponseEntity.status(201).body(commentService.createComment(cardId, commentRequestDto, userDetails.getUser()));
    }

    @PutMapping("/todos/{cardId}/comments/{commentId}")
    public CommentResponseDto editComment(@PathVariable Long cardId, @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.editComment(commentId, commentRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/todos/{cardId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long cardId, @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.status(200).body("댓글이 삭제 되었습니다.");
    }
}
