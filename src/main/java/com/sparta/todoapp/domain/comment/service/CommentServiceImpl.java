package com.sparta.todoapp.domain.comment.service;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.comment.repository.CommentRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.global.exception.CustomException;
import com.sparta.todoapp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CardRepository cardRepository;

    @Override
    public CommentResponseDto createComment(Long cardId, CommentRequestDto commentRequestDto,
            User user) {

        Card card = getCardByCardId(cardId);

        Comment comment = Comment.createComment(commentRequestDto.getContent(), user, card);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto editComment(Long commentId,
            CommentRequestDto commentRequestDto, User user) {

        Comment comment = getCommentByCommentId(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            comment.editComment(commentRequestDto.getContent());
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_EDIT_ONLY_WRITER);
        }

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long commentId, User user) {

        Comment comment = getCommentByCommentId(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new CustomException(ExceptionCode.FORBIDDEN_DELETE_ONLY_WRITER);
        }
    }

    private Card getCardByCardId(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_TODO)
        );
    }

    private Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );
    }
}
