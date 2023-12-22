package com.sparta.todoapp.mvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import com.sparta.todoapp.domain.card.entity.Card;
import com.sparta.todoapp.domain.card.service.CardService;
import com.sparta.todoapp.domain.comment.controller.CommentController;
import com.sparta.todoapp.domain.comment.dto.CommentRequestDto;
import com.sparta.todoapp.domain.comment.dto.CommentResponseDto;
import com.sparta.todoapp.domain.comment.entity.Comment;
import com.sparta.todoapp.domain.comment.repository.CommentRepository;
import com.sparta.todoapp.domain.comment.service.CommentService;
import com.sparta.todoapp.domain.user.controller.UserController;
import com.sparta.todoapp.domain.user.dto.SignupRequestDto;
import com.sparta.todoapp.domain.user.entity.User;
import com.sparta.todoapp.domain.user.entity.UserRoleEnum;
import com.sparta.todoapp.domain.user.service.UserService;
import com.sparta.todoapp.global.config.WebSecurityConfig;
import com.sparta.todoapp.global.security.UserDetailsImpl;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {UserController.class, CardController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
// ex. 밑의 Test 가 누구의 코드를 테스트 하는 것인가?
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

    @MockBean
    CommentRepository commentRepository;

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

    @DisplayName("유저 테스트 모음")
    @Nested
    class userTests {

        @Test
        @DisplayName("회원 가입 테스트")
        void test1() throws Exception {
            // given
            String username = "username";
            String password = "password";
            SignupRequestDto signupRequestDto = new SignupRequestDto(username, password);
            String signupRequestJson = objectMapper.writeValueAsString(signupRequestDto);

            // when - then
            mvc.perform(post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signupRequestJson)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString("회원가입이 완료 되었습니다.")));
        }
    }

    @Nested
    @DisplayName("투 두 카드 테스트 모음")
    class cardTests {

        @Test
        @DisplayName("카드 생성 테스트")
        void test2() throws Exception {
            FileInputStream fileInputStream = new FileInputStream("src/test/java/com/sparta/todoapp/mvc/testfile.jpg");
            // given
            mockUserSetup();
            String title = "제목";
            String content = "내용";
            MockMultipartFile image = new MockMultipartFile(
                    "file",
                    "testfile.jpg",
                    "image/jpg",
                    fileInputStream
            );

            CardResponseDto cardResponseDto = new CardResponseDto();
            cardResponseDto.setAuthor("username");
            cardResponseDto.setTitle(title);
            cardResponseDto.setContent(content);
            cardResponseDto.setImageUrl(null);
            cardResponseDto.setIsDone(false);
            String exTitle = "$.title";
            given(cardService.createTodoCard(any(CardPostRequestDto.class), any(User.class)))
                    .willReturn(cardResponseDto);

            // when - then
            mvc.perform(multipart("/todos")
                            .file(image)
                            .param("title", title)
                            .param("content", content)
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath(exTitle, is(title)));
        }

        @Test
        @DisplayName("카드 수정 테스트")
        void test3() throws Exception {
            // Given
            mockUserSetup();
            String title = "수정제목";
            String content = "수정내용";
            FileInputStream fileInputStream = new FileInputStream("src/test/java/com/sparta/todoapp/mvc/testfile.jpg");
            MockMultipartFile image = new MockMultipartFile(
                    "file",
                    "testfile.jpg",
                    "image/jpg",
                    fileInputStream
            );

            CardResponseDto cardResponseDto = new CardResponseDto();
            cardResponseDto.setTitle(title);
            cardResponseDto.setContent(content);
            cardResponseDto.setAuthor("username");
            cardResponseDto.setImageUrl(null);
            cardResponseDto.setIsDone(false);
            String exTitle = "$.title";
            String exContent = "$.content";
            given(cardService.editTodoCard(any(CardPostRequestDto.class), any(Long.class),
                    any(User.class))).willReturn(cardResponseDto);
            // When & Then
            mvc.perform(multipart("/todos/1")
                            .file(image)
                            .param("title", title)
                            .param("content", content)
                            .with(request -> {
                                request.setMethod("PUT");
                                return request;
                            })
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(exTitle, is(title)))
                    .andExpect(jsonPath(exContent, is(content)));
        }

        @Test
        @DisplayName("카드 상태 변경 테스트")
        void test4() throws Exception {
            // Given
            mockUserSetup();
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
    }


    @Nested
    @DisplayName("댓글 테스트 모음")
    class commentTests {

        @Test
        @DisplayName("댓글 생성 테스트")
        void 댓글_생성_테스트() throws Exception {
            // Given
            mockUserSetup();
            String content = "댓글내용";
            Long commentId = 1L;
            CommentRequestDto requestDto = new CommentRequestDto();
            requestDto.setContent(content);
            CommentResponseDto responseDto = new CommentResponseDto();
            responseDto.setAuthor("username");
            responseDto.setContent("댓글내용");
            String requestJSON = objectMapper.writeValueAsString(requestDto);
            String findAuthorAtResponse = "$.author";
            String findContentAtResponse = "$.content";
            given(commentService.createComment(eq(commentId), any(CommentRequestDto.class),
                    any(User.class))).willReturn(responseDto);
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

        @Test
        @DisplayName("댓글 수정 테스트")
        void 댓글_수정_테스트() throws Exception {
            //given
            mockUserSetup();
            String content = "수정댓글";
            Long commentId = 1L;
            CommentRequestDto requestDto = new CommentRequestDto();
            requestDto.setContent(content);
            String requestJson = objectMapper.writeValueAsString(requestDto);
            CommentResponseDto responseDto = new CommentResponseDto();
            responseDto.setAuthor("username");
            responseDto.setContent("댓글내용");
            String findAuthorAtResponse = "$.author";
            String findContentAtResponse = "$.content";

            given(commentService.editComment(eq(commentId), any(CommentRequestDto.class),
                    any(User.class))).willReturn(responseDto);
            // when & then
            mvc.perform(put("/todos/1/comments/1")
                            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                            .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                            .content(requestJson)
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(findAuthorAtResponse, is("username")))
                    .andExpect(jsonPath(findContentAtResponse, is("댓글내용")));
        }

        @Test
        @DisplayName("댓글 삭제 테스트")
        void 댓글_삭제_테스트() throws Exception {
            //given
            mockUserSetup();
            String username = "username";
            String password = "password";
            UserRoleEnum role = UserRoleEnum.USER;
            User user = new User(username, password, role);
            String cardTitle = "제목";
            String cardContent = "내용";
            Card card = Card.builder().title(cardTitle).content(cardContent).user(user).build();
            String commentContent = "댓글내용";
            Long commentId = 1L;
            Comment comment = new Comment(commentContent, user, card);
            comment.setId(commentId);
            commentRepository.save(comment);
            // when & then
            mvc.perform(delete("/todos/1/comments/1")
                            .principal(mockPrincipal)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("댓글이 삭제 되었습니다.")));
        }
    }
}
