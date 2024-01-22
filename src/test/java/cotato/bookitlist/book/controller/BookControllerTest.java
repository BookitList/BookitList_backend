package cotato.bookitlist.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.book.dto.request.BookRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @DisplayName("[Api] keyword가 주어지면 응답을 준다.")
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
                .andExpect(jsonPath("$.bookApiList").exists())
        ;
    }

    @Test
    @DisplayName("[Api] isbn13이 주어지면 응답을 준다.")
    void givenIsbn13_whenExternalSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String isbn13 = "9791197045318";

        //when&then
        mockMvc.perform(get("/books/external")
                        .param("isbn13", isbn13)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.startIndex").value(1))
                .andExpect(jsonPath("$.itemsPerPage").value(1))
                .andExpect(jsonPath("$.bookApiList").exists())
        ;
    }

    @Test
    @DisplayName("[Api] isbn13과 keyword가 주어지면 응답을 준다.")
    void givenIsbn13AndKeyword_whenExternalSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String isbn13 = "9791197045318";
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/books/external")
                        .param("isbn13", isbn13)
                        .param("keyword", keyword)
                        .param("start", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.startIndex").value(1))
                .andExpect(jsonPath("$.itemsPerPage").value(1))
                .andExpect(jsonPath("$.bookApiList").exists())
        ;
    }

    @Test
    @DisplayName("아무것도 주어지지 않으면 에러를 반환한다.")
    void givenNothing_whenExternalSearching_thenReturnErrorResponse() throws Exception {
        //given

        //when&then
        mockMvc.perform(get("/books/external")
                        .param("start", "1")
                )
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("start만 주어지면 에러를 반환한다.")
    void givenStart_whenExternalSearching_thenReturnErrorResponse() throws Exception {
        //given

        //when&then
        mockMvc.perform(get("/books/external"))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
                .andExpect(jsonPath("$.message").value("이미 등록된 isbn13입니다."))
        ;
    }

    @Test
    @WithMockUser
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

    @Test
    @DisplayName("[DB] isbn13이 주어지면 응답을 준다.")
    void givenIsbn13_whenSearchingBook_thenReturnBookResponse() throws Exception {
        //given
        String isbn13 = "9788931514810";

        //when&then
        mockMvc.perform(get("/books")
                        .param("isbn13", isbn13)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.startIndex").value(0))
                .andExpect(jsonPath("$.itemsPerPage").value(1))
                .andExpect(jsonPath("$.bookList").exists())
        ;
    }

    @Test
    @DisplayName("[DB] 등록되지 않은 isbn13이 주어지면 에러를 응답한다.")
    void givenUnRegisteredIsbn13_whenSearchingBook_thenReturnErrorResponse() throws Exception {
        //given
        String isbn13 = "9788966262281";

        //when&then
        mockMvc.perform(get("/books")
                        .param("isbn13", isbn13)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("등록되지 않은 isbn13입니다."))
        ;
    }

    @Test
    @DisplayName("[DB] keyword이 주어지면 응답을 준다.")
    void givenKeyword_whenSearchingBook_thenReturnBookResponse() throws Exception {
        //given
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/books")
                        .param("keyword", keyword)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(3))
                .andExpect(jsonPath("$.startIndex").value(0))
                .andExpect(jsonPath("$.itemsPerPage").value(3))
                .andExpect(jsonPath("$.bookList").exists())
                .andDo(print())
        ;
    }

}
