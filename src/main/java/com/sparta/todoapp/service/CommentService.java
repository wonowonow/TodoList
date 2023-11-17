package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.comment.CommentRequestDto;
import com.sparta.todoapp.dto.comment.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentService(CommentRepository commentRepository, CardRepository cardRepository) {
        this.commentRepository = commentRepository;
        this.cardRepository = cardRepository;
    }

    public CommentResponseDto createComment(Long cardId, CommentRequestDto commentRequestDto, User user) {
        String content = commentRequestDto.getContent();
        Card card = cardRepository.getReferenceById(cardId);
        Comment comment = new Comment(content, user, card);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;
    }
}
