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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void givenIsbn13_whenRegisteringLike_thenRegisterLike() throws Exception {
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
    void givenIsbn13NonExistedInDB_whenRegisteringLike_thenRegisterLike() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788966262281");

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
}

