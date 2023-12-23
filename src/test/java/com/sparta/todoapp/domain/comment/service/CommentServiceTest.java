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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
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
        commentService = new CommentServiceImpl(commentRepository, cardRepository);
        user = new User("username", "password", UserRoleEnum.USER);
        user.setId(1L);
        card = Card.builder().title("제목").content("내용").user(user).build();
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

    @Test
    @DisplayName("댓글 수정 테스트")
    void 한글로도_테스트이름_설정_가능() {
        // given
        String content = "내용";
        Comment comment = new Comment(content, user, card);
        comment.setId(1L);
        Long commentId = comment.getId();
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("수정");
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        // when
        CommentResponseDto result = commentService.editComment(commentId, requestDto, user);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestDto.getContent(), result.getContent());
        Assertions.assertEquals(user.getUsername(), result.getAuthor());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void 댓글_삭제() {
        // given
        Comment comment = new Comment("내용", user, card);
        comment.setId(1L);
        Long commentId = comment.getId();
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        // when
        commentService.deleteComment(commentId, user);
        // then
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }
}