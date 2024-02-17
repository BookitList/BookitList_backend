package cotato.bookitlist.post.controller;

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
@DisplayName("게시글 좋아요 컨트롤러 테스트")
@ActiveProfiles("test")
class PostLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 좋아요를 생성한다")
    void givenPostId_whenRegisteringPostLike_thenRegisterPostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/posts/12/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("중복된 좋아요를 요청하면 에러를 반환한다.")
    void givenExistedPostLike_whenRegisteringPostLike_thenErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/posts/2/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("게시글 좋아요가 이미 존재합니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않는 게시글에 좋아요를 요청하면 에러를 반환한다.")
    void givenNonExistedPostId_whenRegisteringPostLike_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/posts/100/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 좋아요를 삭제한다.")
    void givenPostLikeId_whenDeletingPostLike_thenDeletePostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/2/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않는 좋아요 삭제 요청시 에러를 반환한다.")
    void givenNonExistedPostLike_whenDeletingPostLike_thenDeletePostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/3/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시글 좋아요 정보를 찾을 수 없습니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("본인의 게시글을 좋아요하면 에러를 반환한다.")
    void givenMyPost_whenRegisteringPostLike_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(post("/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("본인의 게시글은 좋아요할 수 없습니다."))
        ;
    }
}
