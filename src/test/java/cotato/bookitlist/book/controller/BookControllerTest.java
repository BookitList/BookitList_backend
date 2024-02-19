package cotato.bookitlist.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cotato.bookitlist.book.dto.request.BookIsbn13Request;
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
    @DisplayName("[Api] keyword 가 주어지면 응답을 준다.")
    void givenKeyword_whenSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/books/search")
                        .param("keyword", keyword)
                        .param("start", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").exists())
                .andExpect(jsonPath("$.startIndex").exists())
                .andExpect(jsonPath("$.itemsPerPage").value(5))
                .andExpect(jsonPath("$.bookApiList").exists())
        ;
    }

    @Test
    @DisplayName("[Api] MaxResults가 주어지면 그 수 만큼 응답을 준다.")
    void givenMaxResults_whenSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/books/search")
                        .param("keyword", keyword)
                        .param("start", "1")
                        .param("max-results", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").exists())
                .andExpect(jsonPath("$.startIndex").exists())
                .andExpect(jsonPath("$.itemsPerPage").value(10))
                .andExpect(jsonPath("$.bookApiList").exists())
        ;
    }

    @Test
    @DisplayName("[Api] start만 주어지면 에러를 반환한다.")
    void givenStart_whenSearching_thenReturnErrorResponse() throws Exception {
        //given

        //when&then
        mockMvc.perform(get("/books/search")
                        .param("start", "1"))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("[Api] 아무것도 주어지지 않으면 에러를 반환한다.")
    void givenNothing_whenSearching_thenReturnErrorResponse() throws Exception {
        //given

        //when&then
        mockMvc.perform(get("/books/search")
                )
                .andExpect(status().isBadRequest())
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
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.publisher").exists())
                .andExpect(jsonPath("$.pubDate").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.link").exists())
                .andExpect(jsonPath("$.isbn13").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.cover").exists())
        ;
    }

    @Test
    @DisplayName("[Api] 아무것도 주어지지 않으면 에러를 반환한다.")
    void givenNothing_whenExternalFinding_thenReturnErrorResponse() throws Exception {
        //given

        //when&then
        mockMvc.perform(get("/books/external"))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("[Api] 잘못된 isbn13을 주면 에러를 반환한다.")
    void givenInvalidIsbn13_whenExternalFinding_thenReturnErrorResponse() throws Exception {
        //given
        String isbn13 = "9791197045319";

        //when&then
        mockMvc.perform(get("/books/external")
                        .param("isbn13", isbn13))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("[DB] isbn13을 통해 책을 등록한다.")
    void givenIsbn13_whenRegisteringBook_thenReturnBookResponse() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9791197045318");

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
    @DisplayName("[DB] 이미 등록된 책을 등록하면 에러를 반환한다.")
    void givenRegisteredIsbn13_whenRegisteringBook_thenReturnErrorResponse() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788931514803");

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
    @DisplayName("[DB] 잘못된 형식의 isbn13으로 등록하면 에러를 반환한다.")
    void givenInvalidIsbn13_whenRegisteringBook_thenReturnErrorResponse() throws Exception {
        //given
        BookIsbn13Request request = new BookIsbn13Request("9788931514invalid");

        //when&then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 형식의 isbn13입니다."))
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
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.publisher").exists())
                .andExpect(jsonPath("$.pubDate").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.link").exists())
                .andExpect(jsonPath("$.isbn13").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.cover").exists())
        ;
    }

    @Test
    @DisplayName("[API] 등록되지 않은 올바른 형식의 isbn13이 주어지면 API 통신을 통해 응답한다.")
    void givenUnRegisteredIsbn13_whenSearchingBook_thenReturnBookResponseFromApi() throws Exception {
        //given
        String isbn13 = "9788966262281";

        //when&then
        mockMvc.perform(get("/books")
                        .param("isbn13", isbn13)
                )
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.publisher").exists())
                .andExpect(jsonPath("$.pubDate").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.link").exists())
                .andExpect(jsonPath("$.isbn13").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.cover").exists())
        ;
    }

    @Test
    @DisplayName("[DB] id를 이용해 책을 찾는다.")
    void givenBookId_whenGettingBook_thenReturnBookResponse() throws Exception {
        //given
        long bookId = 1L;

        //when&then
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.publisher").exists())
                .andExpect(jsonPath("$.pubDate").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.link").exists())
                .andExpect(jsonPath("$.isbn13").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.cover").exists())
        ;
    }

    @Test
    @DisplayName("[DB] 존재하지 않는 id를 이용해 책을 찾으면 에러를 반환한다.")
    void givenNonExistedId_whenGettingBook_thenReturnErrorResponse() throws Exception {
        //given
        long bookId = 100L;

        //when&then
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("책을 찾을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("[DB] 추천할 책을 랜덤으로 3개 고른다.")
    void givenRecommendCount_whenGettingRecommendBook_thenReturnRecommendBook() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/books/recommend").
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookList.length()").value(3));
    }
}
