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


public interface CommentService {


    CommentResponseDto createComment(Long cardId, CommentRequestDto commentRequestDto,
            User user);

    CommentResponseDto editComment(Long commentId,
            CommentRequestDto commentRequestDto, User user);

    void deleteComment(Long commentId, User user);
}
