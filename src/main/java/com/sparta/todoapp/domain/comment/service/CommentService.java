package com.sparta.todoapp.domain.comment.service;

import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.comment.repository.CommentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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

    public void editComment(Long commentId,
            CommentRequestDto commentRequestDto, User user) {
        List<Comment> commentList = commentRepository.findAllByUser(user);
        for(Comment comment : commentList) {
            if (comment.getId().equals(commentId)) {
                comment.setContent(commentRequestDto.getContent());
                commentRepository.save(comment);
            }
        }
    }

    public void deleteComment(Long commentId, User user) {
        List<Comment> commentList = commentRepository.findAllByUser(user);
        for(Comment comment : commentList) {
            if (comment.getId().equals(commentId)) {
                commentRepository.delete(comment);
            }
        }
    }
}
