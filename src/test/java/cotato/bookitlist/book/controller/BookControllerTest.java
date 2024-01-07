package cotato.bookitlist.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.book.dto.request.BookRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("도서 컨트롤러 테스트")
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("keyword가 주어지면 응답을 준다.")
    void givenKeyword_whenExternalSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/books/external")
                        .param("keyword", keyword)
                        .param("start", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").exists())
                .andExpect(jsonPath("$.startIndex").exists())
                .andExpect(jsonPath("$.itemsPerPage").exists())
                .andExpect(jsonPath("$.bookApiDto").exists())
        ;
    }

    @Test
    @DisplayName("isbn13을 통해 책을 등록한다.")
    void givenIsbn13_whenRegisteringBook_thenReturnBookResponse() throws Exception {
        //given
        BookRegisterRequest request = new BookRegisterRequest("9791197045318");

        //when&then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
        ;
    }

    @Test
    @DisplayName("이미 등록된 책을 등록하면 에러를 반환한다.")
    void givenRegisteredIsbn13_whenRegisteringBook_thenReturnErrorResponse() throws Exception {
        //given
        BookRegisterRequest request = new BookRegisterRequest("9788931514803");

        //when&then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("이미 등록된 isbn13입니다."))
        ;
    }

    @Test
    @DisplayName("잘못된 형식의 isbn13으로 등록하면 에러를 반환한다.")
    void givenInvalidIsbn13_whenRegisteringBook_thenReturnErrorResponse() throws Exception {
        //given
        BookRegisterRequest request = new BookRegisterRequest("9788931514invalid");

        //when&then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
        ;
    }

}
