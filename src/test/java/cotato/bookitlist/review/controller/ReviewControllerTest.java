package cotato.bookitlist.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
import cotato.bookitlist.review.dto.request.ReviewUpdateRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    @DisplayName("한줄요약을 생성한다")
    void givenReviewRegisterRequest_whenRegisteringReview_thenRegisterRegister() throws Exception {
        //given
        ReviewRegisterRequest request = new ReviewRegisterRequest(1L, "content");

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
    @DisplayName("존재하지 않은 책으로 게시글을 생성요청하면 에러를 반환한다.")
    void givenNonExistedBookId_whenRegisteringReview_thenReturnErrorResponse() throws Exception {
        //given
        ReviewRegisterRequest request = new ReviewRegisterRequest(100L, "content");

        //when & then
        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isNotFound())
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
                new ReviewRegisterRequest(1L, ""),
                new ReviewRegisterRequest(1L, tooLongContent)
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약을 수정한다.")
    void givenReviewUpdateRequest_whenUpdatingReview_thenUpdateReview() throws Exception {
        //given
        ReviewUpdateRequest request = new ReviewUpdateRequest("updateContent");

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
        ReviewUpdateRequest request = new ReviewUpdateRequest( "updateContent");

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
                new ReviewUpdateRequest(""),
                new ReviewUpdateRequest(tooLongContent)
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
                .andExpect(jsonPath("$.totalResults").value(6))
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
}
