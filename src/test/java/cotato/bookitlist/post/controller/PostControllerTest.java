package cotato.bookitlist.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.post.dto.requeset.PostRegisterRequest;
import cotato.bookitlist.post.dto.requeset.PostUpdateRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cotato.bookitlist.post.domain.PostStatus.PUBLIC;
import static cotato.bookitlist.post.domain.PostTemplate.NON;
import static cotato.bookitlist.post.domain.PostTemplate.TEMPLATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("게시글 컨트롤러 테스트")
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("DB에 등록된 책에 게시글을 생성한다")
    void givenPostRegisterRequest_whenRegisteringPost_thenRegisterPost() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "content", PUBLIC, NON);

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("Template을 활용하여 게시글을 생성한다")
    void givenTemplateRequest_whenRegisteringPost_thenRegisterPost() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "first<============================>second<============================>third<============================>fourth", PUBLIC, TEMPLATE);

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("잘못된 split 문자열이 오면 에러를 반환한다.")
    void givenInvalidSplitString_whenRegisteringPost_thenReturnErrorResponse() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "first<===========================>second<============================>third<============================>fourth", PUBLIC, TEMPLATE);

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("DB에 존재하지 않는 책으로 게시글을 생성요청하면 API 통신을 통해 책을 등록하고 게시글을 등록한다.")
    void givenNonExistedInDataBaseIsbn13_whenRegisteringPost_thenRegisterBookAndPost() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest("9791193235119", "title", "content", PUBLIC, NON);

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않은 책으로 게시글을 생성요청하면 에러를 반환한다.")
    void givenNonExistedIsbn13_whenRegisteringPost_thenReturnErrorResponse() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest("9782345678908", "title", "content", PUBLIC, NON);

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
        ;
    }

    @ParameterizedTest
    @WithCustomMockUser
    @MethodSource("provideInvalidPostRequest")
    @DisplayName("게시글 생성시 title과 content의 값 예외 검사")
    void givenInvalidTitleOrContent_whenRegisteringPost_thenReturnErrorResponse(PostRegisterRequest request) throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    private static List<PostRegisterRequest> provideInvalidPostRequest() {
        String tooLongTitle = "TooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooLongTitle";

        return List.of(
                new PostRegisterRequest("9788931514810", "", "content", null, NON),
                new PostRegisterRequest("9788931514810", "", "content", PUBLIC, NON),
                new PostRegisterRequest("9788931514810", "title", "", PUBLIC, NON),
                new PostRegisterRequest("9788931514810", "", "", PUBLIC, NON),
                new PostRegisterRequest("9788931514810", tooLongTitle, "", PUBLIC, NON),
                new PostRegisterRequest("9788931514810", tooLongTitle, "content", PUBLIC, NON)
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글을 수정한다.")
    void givenPostUpdateRequest_whenUpdatingPost_thenUpdatePost() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent", PUBLIC, NON);

        //when & then
        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("권한이 없는 게시글을 수정하면 에러를 반환한다.")
    void givenInvalidMemberId_whenUpdatingPost_thenReturnErrorResponse() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent", PUBLIC, NON);

        //when & then
        mockMvc.perform(put("/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @ParameterizedTest
    @WithCustomMockUser
    @MethodSource("provideInvalidPostUpdateRequest")
    @DisplayName("게시글 수정시 title과 content의 값 예외 검사")
    void givenInvalidTitleOrContent_whenUpdatingPost_thenReturnErrorResponse(PostUpdateRequest request) throws Exception {
        //given

        //when & then
        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    private static List<PostUpdateRequest> provideInvalidPostUpdateRequest() {
        String tooLongTitle = "TooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooLongTitle";

        return List.of(
                new PostUpdateRequest("title", "content", null, NON),
                new PostUpdateRequest("", "content", PUBLIC, NON),
                new PostUpdateRequest("title", "", PUBLIC, NON),
                new PostUpdateRequest("", "", PUBLIC, NON),
                new PostUpdateRequest(tooLongTitle, "", PUBLIC, NON),
                new PostUpdateRequest(tooLongTitle, "content", PUBLIC, NON)
        );
    }

    @Test
    @DisplayName("게시글을 조회한다.")
    void givenPostId_whenGettingPost_thenPostResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("쿠키없이 게시글을 조회하면 쿠키를 생성한다.")
    void givenNonCookie_whenGettingPost_thenCreateCookie() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value("post_view", "[1]"))
                .andExpect(cookie().path("post_view", "/posts"))
        ;
    }

    @Test
    @DisplayName("쿠키를 가지고 게시글을 조회하면 쿠키에 id를 추가하여 넘겨준다.")
    void givenCookie_whenGettingPost_thenAddIdIntoCookie() throws Exception {
        //given
        Cookie cookie = new Cookie("post_view", "[1]");

        //when & then
        mockMvc.perform(get("/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(cookie().value("post_view", "[1][2]"))
                .andExpect(cookie().path("post_view", "/posts"))
        ;
    }


    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 본인 게시글을 조회한다.")
    void givenPostIdWithLogin_whenGettingPost_thenPostResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMine").value(true))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 다른 사람 게시글을 조회한다.")
    void givenPostIdWithLogin_whenGettingAnotherPersonPost_thenPostResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMine").value(false))
        ;
    }

    @Test
    @DisplayName("없는 게시글 id로 조회하면 에러를 반환한다.")
    void givenNonExistedPostId_whenGettingPost_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("isbn13을 이용해 게시글을 조회한다.")
    void givenIsbn13_whenSearchingPost_thenReturnPostListResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/posts")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.postList[1].liked").value(false))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 isbn13을 이용해 게시글을 조회한다.")
    void givenIsbn13WithLogin_whenSearchingPost_thenReturnPostListResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/posts")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.postList[1].liked").value(true))
        ;
    }

    @Test
    @DisplayName("전체 게시글을 조회한다.")
    void givenNothing_whenSearchingPost_thenReturnPostListResponse() throws Exception {
        //given
        //when & then
        mockMvc.perform(get("/posts/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(8))
        ;
    }

    @Test
    @DisplayName("isbn13을 이용해 게시글 count를 조회한다.")
    void givenIsbn13_whenCountingPost_thenReturnPostCountResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/posts/count")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 id를 이용해 게시글을 삭제한다.")
    void givenPostId_whenDeletingPost_thenDeletePost() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 id를 이용해 좋아요가 없는 게시글을 삭제한다.")
    void givenPostIdNonPostLike_whenDeletingPost_thenDeletePost() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 id를 이용해 비공개 게시글을 삭제한다.")
    void givenPostIdNonPrivate_whenDeletingPost_thenDeletePost() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

}

