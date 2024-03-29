package cotato.bookitlist.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.review.domain.ReviewStatus;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("한줄요약 컨트롤러 테스트")
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("DB에 등록된 책에 한줄요약을 생성한다")
    void givenReviewRegisterRequest_whenRegisteringReview_thenRegisterRegister() throws Exception {
        //given
        ReviewRegisterRequest request = new ReviewRegisterRequest("9788931514810", "content", ReviewStatus.PUBLIC);

        //when & then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("DB에 존재하지 않는 책으로 한줄요약을 생성요청하면 API 통신을 통해 책을 등록하고 한줄요약을 등록한다.")
    void givenNonExistedInDataBaseIsbn13_whenRegisteringReview_thenRegisterBookAndReview() throws Exception {
        //given
        ReviewRegisterRequest request = new ReviewRegisterRequest("9791193235119", "content", ReviewStatus.PUBLIC);

        //when & then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않은 책으로 한줄요약을 생성요청하면 에러를 반환한다.")
    void givenNonExistedIsbn13_whenRegisteringReview_thenReturnErrorResponse() throws Exception {
        //given
        ReviewRegisterRequest request = new ReviewRegisterRequest("9782345678908", "content", ReviewStatus.PUBLIC);

        //when & then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
        ;
    }

    @ParameterizedTest
    @WithCustomMockUser
    @MethodSource("provideInvalidReviewRequest")
    @DisplayName("한줄요약 생성시 content의 값의 예외을 검사한다.")
    void givenInvalidContent_whenRegisteringReview_thenReturnErrorResponse(ReviewRegisterRequest request) throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    private static List<ReviewRegisterRequest> provideInvalidReviewRequest() {
        String tooLongContent = "TooooooooooooooooooooooooooooooooooooooooooooooLong"; // 51글자

        return List.of(
                new ReviewRegisterRequest("9788931514810", "", ReviewStatus.PUBLIC),
                new ReviewRegisterRequest("9788931514810", tooLongContent, ReviewStatus.PUBLIC)
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약을 수정한다.")
    void givenReviewUpdateRequest_whenUpdatingReview_thenUpdateReview() throws Exception {
        //given
        ReviewUpdateRequest request = new ReviewUpdateRequest("updateContent", ReviewStatus.PUBLIC);

        //when & then
        mockMvc.perform(put("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("권한이 없는 한줄요약를 수정하면 에러를 반환한다.")
    void givenInvalidMemberId_whenUpdatingReview_thenReturnErrorResponse() throws Exception {
        //given
        ReviewUpdateRequest request = new ReviewUpdateRequest("updateContent", ReviewStatus.PUBLIC);

        //when & then
        mockMvc.perform(put("/reviews/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @ParameterizedTest
    @WithCustomMockUser
    @MethodSource("provideInvalidReviewUpdateRequest")
    @DisplayName("한줄요약 수정시 content 값 예외 검사")
    void givenInvalidContent_whenUpdatingReview_thenReturnErrorResponse(ReviewUpdateRequest request) throws Exception {
        //given

        //when & then
        mockMvc.perform(put("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    private static List<ReviewUpdateRequest> provideInvalidReviewUpdateRequest() {
        String tooLongContent = "TooooooooooooooooooooooooooooooooooooooooooooooLong"; // 51글자

        return List.of(
                new ReviewUpdateRequest("", ReviewStatus.PUBLIC),
                new ReviewUpdateRequest(tooLongContent, ReviewStatus.PUBLIC)
        );
    }

    @Test
    @DisplayName("한줄요약을 조회한다.")
    void givenReviewId_whenGettingReview_thenReturnReviewResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("쿠키없이 한줄요약을 조회하면 쿠키를 생성하다.")
    void givenNonCookie_whenGettingReview_thenCreateCookie() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value("review_view", "[1]"))
                .andExpect(cookie().path("review_view", "/reviews"));
    }

    @Test
    @DisplayName("쿠키를 가지고 한줄요약을 조회하면 쿠키에 id를 추가하여 넘겨준다.")
    void givenCookie_whenGettingReview_thenAddIdIntoCookie() throws Exception {
        //given
        Cookie cookie = new Cookie("review_view", "[1]");

        //when & then
        mockMvc.perform(get("/reviews/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(cookie().value("review_view", "[1][2]"))
                .andExpect(cookie().path("review_view", "/reviews"));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 본인 한줄요약을 조회한다.")
    void givenReviewIdWithLogin_whenGettingMyReview_thenReviewResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMine").value(true));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 다른 사람 한줄요약을 조회한다.")
    void givenReviewIdWithLogin_whenGettingAnotherPersonReview_thenReviewResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMine").value(false));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 본인의 private 게시글을 조회한다.")
    void givenReviewIdWithLogin_whenGettingMyPrivateReview_thenReturnReviewResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PRIVATE"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 다른 사람 private 한줄요약을 조회하면 에러를 반환한다.")
    void givenReviewIdWithLogin_whenGettingPrivateReview_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("한줄요약을 찾을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("로그인 안된 유저가 private 한줄요약을 조회하면 에러를 반환한다.")
    void givenReviewId_whenGettingPrivateReview_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("한줄요약을 찾을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("없는 한줄요약 id로 조회하면 에러를 반환한다.")
    void givenNonExistedReviewId_whenGettingReview_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("isbn13을 이용해 한줄요약을 조회한다.")
    void givenIsbn13_whenSearchingReview_thenReturnReviewListResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/reviews")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.reviewList[1].liked").value(false))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 isbn13을 이용해 한줄요약을 조회한다.")
    void givenIsbn13WithLogin_whenSearchingReview_thenReturnReviewListResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/reviews")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.reviewList[1].liked").value(true))
        ;
    }

    @Test
    @DisplayName("전체 한줄요약을 조회한다.")
    void givenNothing_whenSearchingReview_thenReturnReviewListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(6))
        ;
    }

    @Test
    @DisplayName("isbn13을 이용해 한줄요약 count를 조회한다.")
    void givenIsbn13_whenCountingReview_thenReturnReviewCountResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/reviews/count")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(4))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약 id를 이용해 좋아요가 있는 한줄요약을 삭제한다.")
    void givenReviewId_whenDeletingReview_thenDeleteReview() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약 id를 이용해 좋아요가 없는 한줄요약을 삭제한다.")
    void givenReviewIdNonReviewLike_whenDeletingReview_thenDeleteReview() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/reviews/7")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약 id를 이용해 비공개 한줄요약을 삭제한다.")
    void givenPrivateReviewId_whenDeletingReview_thenDeleteReview() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/reviews/8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 memberId를 이용해 한줄요약을 조회한다.")
    void givenMemberIdWithLogin_whenSearchingReview_thenReturnReviewListResponse() throws Exception {
        //given
        Long memberId = 2L;

        //when & then
        mockMvc.perform(get("/reviews")
                        .param("member-id", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.reviewList[0].liked").value(true))
        ;
    }

    @Test
    @DisplayName("로그인하지 않은 유저가 memberId를 이용해 한줄요약을 조회한다.")
    void givenMemberId_whenSearchingReview_thenReturnReviewListResponse() throws Exception {
        //given
        Long memberId = 2L;

        //when & then
        mockMvc.perform(get("/reviews")
                        .param("member-id", String.valueOf(memberId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalResults").value(4))
                .andExpect(jsonPath("$.reviewList[0].liked").value(false))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("유저가 좋아요한 한줄요약을 조회한다.")
    void givenLogin_whenSearchingLikeReview_thenReturnReviewListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.reviewList[0].reviewId").value(2))
        ;
    }

    @Test
    @DisplayName("로그인 없이 좋아요한 한줄요약 조회시 에러를 반환한다.")
    void givenNonLogin_whenSearchingLikeReview_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("유저가 본인이 작성한 한줄요약을 조회한다.")
    void givenLogin_whenGettingMyReview_thenReturnReviewListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(3))
        ;
    }

    @Test
    @DisplayName("로그인 없이 본인이 작성한 한줄요약을 조회하면 에러를 반환한다.")
    void givenNonLogin_whenGettingMyReview_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/reviews/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @DisplayName("좋아요가 많은 순으로 한줄요약을 4개 반환한다.")
    void givenPageStartAndRecommendType_whenGettingMostLikeReviews_thenReturnMostLikeReviews() throws Exception {
        //given
        int start = 0;
        String type = "LIKE";

        //when & then
        mockMvc.perform(get("/reviews/recommend")
                        .param("start", String.valueOf(start))
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewList.length()").value(4))
                .andExpect(jsonPath("$.reviewList[0].likeCount").value(3))
                .andExpect(jsonPath("$.reviewList[1].likeCount").value(2))
        ;
    }

    @Test
    @DisplayName("최신 순으로 한줄요약을 4개 반환한다.")
    void givenPageStartAndRecommendType_whenGettingNewReviews_thenReturnNewReviews() throws Exception {
        //given
        int start = 0;
        String type = "NEW";

        //when & then
        mockMvc.perform(get("/reviews/recommend")
                        .param("start", String.valueOf(start))
                        .param("type", type)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewList.length()").value(4))
                .andExpect(jsonPath("$.reviewList[0].reviewId").value(1))
                .andExpect(jsonPath("$.reviewList[1].reviewId").value(2))
        ;
    }

    @Test
    @DisplayName("isbn을 이용해 책의 가장 좋아요가 많은 한줄요약을 간략하게 반환한다.")
    void givenIsbn13_whenGettingBestReviewOfBook_thenReturnBestReviewOfBook() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/reviews/best")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(2))
        ;
    }

    @Test
    @DisplayName("한줄요약이 없는 책에 isbn을 이용해 가장 좋아요가 많은 한줄요약을 조회하면 에러를 반환한다.")
    void givenIsbn13NonReview_whenGettingBestReviewOfBook_thenReturnError() throws Exception {
        //given
        String isbn13 = "9791127278199";

        //when & then
        mockMvc.perform(get("/reviews/best")
                        .param("isbn13", isbn13)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("한줄요약을 찾을 수 없습니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("본인의 한줄요약 상태를 바꾼다")
    void givenWithLogin_whenTogglingReviewStatus_thenToggleReviewStatus() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/reviews/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("다른 사람 한줄요약 상태를 바꾼다")
    void givenNonMyReview_whenTogglingReviewStatus_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/reviews/2/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("로그인 없이 한줄요약 상태를 바꾼면 에러를 반환한다.")
    void givenWithNonLogin_whenTogglingReviewStatus_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/reviews/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }
}
