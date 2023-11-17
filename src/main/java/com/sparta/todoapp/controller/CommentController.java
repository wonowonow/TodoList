package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.comment.CommentRequestDto;
import com.sparta.todoapp.dto.comment.CommentResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/todos/{cardId}/comments")
    public CommentResponseDto createComment(@PathVariable Long cardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        return commentService.createComment(cardId, commentRequestDto, userDetails.getUser());
    }

}
