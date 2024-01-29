package cotato.bookitlist.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.review.dto.request.ReviewRegisterRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
