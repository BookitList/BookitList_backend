package cotato.bookitlist.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.annotation.WithCustomMockUser;
import cotato.bookitlist.book.dto.request.BookIsbn13Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("도서 좋아요 컨트롤러 테스트")
@ActiveProfiles("test")
class BookLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("도서 좋아요를 등록한다.")
    void givenIsbn13_whenRegisteringBookLike_thenRegisterBookLike() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9791127278199");

        //when & then
        mockMvc.perform(post("/books/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("DB에 등록되지 않은 도서 좋아요를 등록한다.")
    void givenIsbn13NonExistedInDB_whenRegisteringBookLike_thenRegisterBookLike() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788966262281");

        //when & then
        mockMvc.perform(post("/books/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("중복된 좋아요를 요청하면 에러를 반환한다.")
    void givenExistedBookLike_whenRegisteringBookLike_thenErrorResponse() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788931514810");

        //when & then
        mockMvc.perform(post("/books/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isConflict())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("좋아요 정보를 삭제한다.")
    void givenIsbn13_whenDeletingBookLike_thenDeleteBookLike() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788931514810");

        //when & then
        mockMvc.perform(delete("/books/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isNoContent())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("존재하지 않는 좋아요 삭제시 에러를 반환한다.")
    void givenNonExistedBookLike_whenDeletingBookLike_thenReturnErrorResponse() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9791187824824");

        //when & then
        mockMvc.perform(delete("/books/likes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("찜한 책 목록을 조회한다.")
    void givenLoginMember_whenGettingLikePosts_thenReturnBookListResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/books/likes/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(2))
        ;
    }

    @Test
    @DisplayName("로그인 없이 찜한 책 목록을 조회하면 에러를 반환한다.")
    void givenNonLoginMember_whenGettingLikePosts_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/books/likes/all"))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 책을 좋아요 했는지 확인한다.")
    void givenLoginMember_whenGettingLiked_thenReturnBookLikeResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/books/likes")
                        .param("isbn13", isbn13))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인한 유저가 isbn13 없이 책을 좋아요를 확인하면 에러를 반환한다.")
    void givenNonIsbn13_whenGettingLiked_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/books/likes"))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("로그인안한 유저가 책을 좋아요 했는지 확인하면 에러를 반환한다.")
    void givenMember_whenGettingLiked_thenReturnErrorResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when & then
        mockMvc.perform(get("/books/likes"))
                .andExpect(status().isUnauthorized())
                .andDo(print())
        ;
    }
}

