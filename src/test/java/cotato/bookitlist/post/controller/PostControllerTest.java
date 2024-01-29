package cotato.bookitlist.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.post.dto.PostRegisterRequest;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("게시글을 생성한다")
    void givenPostRegisterRequest_whenRegisteringPost_thenRegisterPost() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest(1L, "title", "content");

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
    void givenNonExistedBookId_whenRegisteringPost_thenReturnErrorResponse() throws Exception {
        //given
        PostRegisterRequest request = new PostRegisterRequest(100L, "title", "content");

        //when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isNotFound())
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
                new PostRegisterRequest(1L, "", "content"),
                new PostRegisterRequest(1L, "title", ""),
                new PostRegisterRequest(1L, "", ""),
                new PostRegisterRequest(1L, tooLongTitle, ""),
                new PostRegisterRequest(1L, tooLongTitle, "content")
        );
    }
}
