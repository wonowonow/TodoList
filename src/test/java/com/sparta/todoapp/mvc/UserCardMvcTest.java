package com.sparta.todoapp.mvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.domain.card.controller.CardController;
import com.sparta.todoapp.domain.card.dto.CardDoneStatusRequestDto;
import com.sparta.todoapp.domain.card.dto.CardPostRequestDto;
import com.sparta.todoapp.domain.card.dto.CardResponseDto;
import com.sparta.todoapp.domain.card.service.CardService;
import com.sparta.todoapp.domain.comment.controller.CommentController;
import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.comment.service.CommentService;
import com.sparta.todoapp.domain.user.controller.UserController;
import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.service.UserService;
import com.sparta.todoapp.global.config.WebSecurityConfig;
import com.sparta.todoapp.global.security.UserDetailsImpl;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
        controllers = {UserController.class, CardController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class UserCardMvcTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    CardService cardService;

    @MockBean
    CommentService commentService;

    UserDetailsImpl testUserDetails;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
    }

    private void mockUserSetup() {
        String username = "username";
        String password = "password";
        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, password, role);
        testUserDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
                testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void test1() throws Exception {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("username");
        signupRequestDto.setPassword("password");
        String signupRequestJson = objectMapper.writeValueAsString(signupRequestDto);

        // when - then
        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("회원가입이 완료 되었습니다.")));
    }

    @Test
    @DisplayName("카드 생성 테스트")
    void test2() throws Exception {
        // given
        this.mockUserSetup();
        CardPostRequestDto cardPostRequestDto = new CardPostRequestDto();
        cardPostRequestDto.setTitle("제목");
        cardPostRequestDto.setContent("내용");
        String cardPostRequestJson = objectMapper.writeValueAsString(cardPostRequestDto);
        CardResponseDto cardResponseDto = new CardResponseDto();
        cardResponseDto.setAuthor("username");
        cardResponseDto.setContent("내용");
        cardResponseDto.setTitle("제목");
        cardResponseDto.setIsDone(false);
        String title = "$.title";
        given(cardService.createTodoCard(any(CardPostRequestDto.class), any(User.class)))
                .willReturn(cardResponseDto);

        // when - then
        mvc.perform(post("/todos")
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(cardPostRequestJson)
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath(title, is("제목")));
    }

    @Test
    @DisplayName("카드 수정 테스트")
    void test3() throws Exception {
        // Given
        this.mockUserSetup();
        CardPostRequestDto requestDto = new CardPostRequestDto();
        requestDto.setTitle("수정제목");
        requestDto.setContent("수정내용");
        String requestJson = objectMapper.writeValueAsString(requestDto);
        CardResponseDto responseDto = new CardResponseDto();
        responseDto.setTitle("수정제목");
        responseDto.setContent("수정내용");
        responseDto.setAuthor("username");
        responseDto.setIsDone(false);
        String title = "$.title";
        String content = "$.content";
        given(cardService.editTodoCard(any(CardPostRequestDto.class), any(Long.class),
                any(User.class))).willReturn(responseDto);
        // When & Then
        mvc.perform(put("/todos/1")
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(requestJson)
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(title, is("수정제목")))
                .andExpect(jsonPath(content, is("수정내용")));
    }

    @Test
    @DisplayName("카드 상태 변경 테스트")
    void test4() throws Exception {
        // Given
        this.mockUserSetup();
        CardDoneStatusRequestDto requestDto = new CardDoneStatusRequestDto();
        requestDto.setIsDone(true);
        CardResponseDto responseDto = new CardResponseDto();
        responseDto.setTitle("제목");
        responseDto.setContent("내용");
        responseDto.setAuthor("username");
        responseDto.setIsDone(true);
        String requestJson = objectMapper.writeValueAsString(requestDto);

        String title = "$.title";
        String content = "$.content";
        String author = "$.author";
        String isDone = "$.isDone";

        given(cardService.changeTodoCardDone(any(Long.class), any(User.class),
                any(CardDoneStatusRequestDto.class)))
                .willReturn(responseDto);
        // When & Then
        mvc.perform(patch("/todos/1")
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(requestJson)
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(title, is("제목")))
                .andExpect(jsonPath(content, is("내용")))
                .andExpect(jsonPath(author, is("username")))
                .andExpect(jsonPath(isDone, is(true)));
    }

    // 여기서 부터 Comment

    @Test
    @DisplayName("댓글 생성 테스트")
    void 댓글_생성_테스트() throws Exception{
        // Given
        mockUserSetup();
        String content = "댓글내용";
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent(content);
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setAuthor("username");
        responseDto.setContent("댓글내용");
        String requestJSON = objectMapper.writeValueAsString(requestDto);
        String findAuthorAtResponse = "$.author";
        String findContentAtResponse = "$.content";
        given(commentService.createComment(any(Long.class), any(CommentRequestDto.class), any(User.class))).willReturn(responseDto);
        // When & Then
        mvc.perform(post("/todos/1/comments")
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .principal(mockPrincipal)
                .content(requestJSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath(findContentAtResponse, is("댓글내용")))
                .andExpect(jsonPath(findAuthorAtResponse, is("username")));
    }
}
