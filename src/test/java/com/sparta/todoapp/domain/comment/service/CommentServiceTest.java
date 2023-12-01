package com.sparta.todoapp.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.repository.CardRepository;
import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.comment.repository.CommentRepository;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CommentRepository commentRepository;

    CommentService commentService;

    User user;

    Card card;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, cardRepository);
        user = new User("username", "password", UserRoleEnum.USER);
        user.setId(1L);
        card = new Card("제목", "내용", user);
        card.setId(1L);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void test1() {
        // given
        Long cardId = card.getId();
        String content = "댓글내용";
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent(content);

        given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
        // when
        CommentResponseDto result = commentService.createComment(cardId, requestDto, user);
        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
        Assertions.assertEquals(content, result.getContent());
        Assertions.assertEquals(user.getUsername(), result.getAuthor());
    }
}