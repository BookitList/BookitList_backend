package cotato.bookitlist.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.post.domain.PostStatus;
import cotato.bookitlist.post.dto.request.PostRegisterRequest;
import cotato.bookitlist.post.dto.request.PostUpdateRequest;
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
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "content", PostStatus.PUBLIC, NON);

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
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "first<============================>second<============================>third<============================>fourth", PostStatus.PUBLIC, TEMPLATE);

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
        PostRegisterRequest request = new PostRegisterRequest("9788931514810", "title", "first<===========================>second<============================>third<============================>fourth", PostStatus.PUBLIC, TEMPLATE);

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
        PostRegisterRequest request = new PostRegisterRequest("9791193235119", "title", "content", PostStatus.PUBLIC, NON);

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
        PostRegisterRequest request = new PostRegisterRequest("9782345678908", "title", "content", PostStatus.PUBLIC, NON);

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
                new PostRegisterRequest("9788931514810", "", "content", PostStatus.PUBLIC, NON),
                new PostRegisterRequest("9788931514810", "title", "", PostStatus.PUBLIC, NON),
                new PostRegisterRequest("9788931514810", "", "", PostStatus.PUBLIC, NON),
                new PostRegisterRequest("9788931514810", tooLongTitle, "", PostStatus.PUBLIC, NON),
                new PostRegisterRequest("9788931514810", tooLongTitle, "content", PostStatus.PUBLIC, NON)
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글을 수정한다.")
    void givenPostUpdateRequest_whenUpdatingPost_thenUpdatePost() throws Exception {
        //given
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent", PostStatus.PUBLIC, NON);

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
        PostUpdateRequest request = new PostUpdateRequest("updateTitle", "updateContent", PostStatus.PUBLIC, NON);

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
                new PostUpdateRequest("", "content", PostStatus.PUBLIC, NON),
                new PostUpdateRequest("title", "", PostStatus.PUBLIC, NON),
                new PostUpdateRequest("", "", PostStatus.PUBLIC, NON),
                new PostUpdateRequest(tooLongTitle, "", PostStatus.PUBLIC, NON),
                new PostUpdateRequest(tooLongTitle, "content", PostStatus.PUBLIC, NON)
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
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 본인의 private 게시글을 조회한다.")
    void givenPostIdWithLogin_whenGettingMyPrivatePost_thenReturnPostResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PRIVATE"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 다른사람 private 게시글을 조회하면 에러를 반환한다.")
    void givenPostIdWithLogin_whenGettingPrivatePost_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/13")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("로그인 안된 유저가 private 게시글을 조회하면 에러를 반환한다.")
    void givenPostId_whenGettingPrivatePost_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/13")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
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

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 memberId를 이용해 게시글을 조회한다.")
    void givenMemberIdWithLogin_whenSearchingPost_thenReturnPostListResponse() throws Exception {
        //given
        Long memberId = 2L;

        //when & then
        mockMvc.perform(get("/posts")
                        .param("member-id", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(6))
                .andExpect(jsonPath("$.postList[0].liked").value(true))
        ;
    }


    @Test
    @DisplayName("로그인 하지 않은 유저가 memberId를 이용해 게시글을 조회한다.")
    void givenMemberId_whenSearchingPost_thenReturnPostListResponse() throws Exception {
        //given
        Long memberId = 2L;

        //when & then
        mockMvc.perform(get("/posts")
                        .param("member-id", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(6))
                .andExpect(jsonPath("$.postList[0].liked").value(false))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("유저가 좋아요한 게시글을 조회한다.")
    void givenLogin_whenSearchingLikePost_thenReturnPostListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(3))
                .andExpect(jsonPath("$.postList[0].postId").value(7))
        ;
    }

    @Test
    @DisplayName("로그인 없이 좋아요한 게시글 요청시 에러를 반환한다.")
    void givenNonLogin_whenSearchingLikePost_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("유저가 본인이 작성한 게시글을 조회한다.")
    void givenLogin_whenGettingMyPost_thenReturnPostListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(3))
        ;
    }

    @Test
    @DisplayName("로그인 없이 본인이 작성한 게시글을 조회하면 에러를 반환한다.")
    void givenNonLogin_whenGettingMyPost_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/posts/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @DisplayName("좋아요가 많은 순으로 게시글을 3개 반환한다.")
    void givenPageStartAndRecommendType_whenGettingMostLikePosts_thenReturnMostLikePosts() throws Exception {
        //given
        int start = 0;
        String type = "LIKE";

        //when & then
        mockMvc.perform(get("/posts/recommend")
                        .param("start", String.valueOf(start))
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postList.length()").value(3))
                .andExpect(jsonPath("$.postList[0].likeCount").value(3))
                .andExpect(jsonPath("$.postList[1].likeCount").value(2))
        ;
    }

    @Test
    @DisplayName("최신 순으로 게시글을 3개 반환한다")
    void givenPageStartAndRecommendType_whenGettingNewPosts_thenReturnNewPosts() throws Exception {
        //given
        int start = 0;
        String type = "NEW";

        //when & then
        mockMvc.perform(get("/posts/recommend")
                        .param("start", String.valueOf(start))
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postList.length()").value(3))
                .andExpect(jsonPath("$.postList[0].postId").value(1))
                .andExpect(jsonPath("$.postList[1].postId").value(2))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("본인의 게시글 상태를 바꾼다")
    void givenWithLogin_whenTogglingPostStatus_thenTogglePostStatus() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/posts/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("다른 사람 게시글 상태를 바꾼다")
    void givenNonMyPost_whenTogglingPostStatus_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/posts/2/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("로그인 없이 게시글 상태를 바꾼면 에러를 반환한다.")
    void givenWithNonLogin_whenTogglingPostStatus_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/posts/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }


}

