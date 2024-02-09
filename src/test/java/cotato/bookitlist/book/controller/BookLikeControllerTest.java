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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("이미 도서 좋아요 정보가 존재하면 에러를 반환한다.")
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
}

