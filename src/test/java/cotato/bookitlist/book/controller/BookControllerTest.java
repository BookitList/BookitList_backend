package cotato.bookitlist.book.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("도서 컨트롤러 테스트")
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("keyword가 주어지면 응답을 준다.")
    void givenKeyword_whenExternalSearching_thenReturnBookApiResponse() throws Exception {
        //given
        String keyword = "aladdin";

        //when&then
        mockMvc.perform(get("/api/books/external")
                        .param("key-word", keyword)
                        .param("start", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").exists())
                .andExpect(jsonPath("$.startIndex").exists())
                .andExpect(jsonPath("$.itemsPerPage").exists())
                .andExpect(jsonPath("$.bookApiDto").exists());
    }

}
