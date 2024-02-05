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
        mockMvc.perform(post("/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 좋아요를 삭제한다.")
    void givenPostLikeId_whenDeletingPostLike_thenDeletePostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/2/likes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("잘못된 게시글 좋아요 삭제요청시 에러를 반환한다.")
    void givenInvalidPostId_whenDeletingPostLike_thenDeletePostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/1/likes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 게시글의 좋아요가 아닙니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("권한이 없는 유저가 좋아요 삭제요청시 에러를 반환한다.")
    void givenInvalidMemberId_whenDeletingPostLike_thenDeletePostLike() throws Exception {
        //given

        //when & then
        mockMvc.perform(delete("/posts/2/likes/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("권한이 없는 유저입니다."))
        ;
    }

}
