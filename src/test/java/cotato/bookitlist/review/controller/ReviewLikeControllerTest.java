package cotato.bookitlist.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("한줄요약 좋아요 컨트롤러 테스트")
@ActiveProfiles("test")
class ReviewLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약 좋아요를 생성한다")
    void givenReviewId_whenRegisteringReviewLike_thenRegisterReviewLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/reviews/3/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("중복된 좋아요를 요청하면 에러를 반환한다")
    void givenExistedReviewLike_whenRegisteringReviewLike_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/reviews/2/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("한줄요약 좋아요가 이미 존재합니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않는 한줄요약에 좋아요를 요청하면 에러를 반환한다")
    void givenNonExistedReviewId_whenRegisteringReviewLike_thenErrorResponse() throws Exception {
        //given

        //when && then
        mockMvc.perform(post("/reviews/10/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("한줄요약을 찾을 수 없습니다."));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("한줄요약 좋아요를 삭제한다.")
    void givenReviewId_whenDeletingReviewLike_thenDeleteReviewLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/reviews/2/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않는 좋아요 삭제 요청시 에러를 반환한다.")
    void givenNonExistedReviewLike_whenDeletingReviewLike_thenDeleteReviewLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/reviews/3/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("한줄요약 좋아요 정보를 찾을 수 없습니다."))
        ;
    }
}
