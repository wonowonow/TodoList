package com.sparta.todoapp.domain.comment.service;

import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.comment.repository.CommentRepository;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentService(CommentRepository commentRepository, CardRepository cardRepository) {
        this.commentRepository = commentRepository;
        this.cardRepository = cardRepository;
    }

    public CommentResponseDto createComment(Long cardId, CommentRequestDto commentRequestDto,
            User user) {

        String content = commentRequestDto.getContent();
        Card card = cardRepository.getReferenceById(cardId);
        Comment comment = new Comment(content, user, card);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return commentResponseDto;
    }

    @Transactional
    public void editComment(Long commentId,
            CommentRequestDto commentRequestDto, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );

        if (comment.getUser().getId().equals(user.getId())) {
            comment.setContent(commentRequestDto.getContent());
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_EDIT_ONLY_WRITER);
        }
    }

    public void deleteComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_DELETE_ONLY_WRITER);
        }
    }
}
