package cotato.bookitlist.member.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("멤버 컨트롤러 테스트")
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("멤버 id를 이용해 멤버를 조회한다.")
    void givenMemberId_whenGettingMember_thenGetMemberInfo() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/members/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMe").value(false))
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.profileLink").exists())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("본인 멤버 id를 이용해 멤버를 조회한다.")
    void givenMyMemberId_whenGettingMember_thenGetMemberInfo() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/members/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMe").value(true))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.profileLink").exists())
        ;
    }

    @Test
    @DisplayName("private 멤버 id를 이용해 멤버를 조회하면 에러를 반환한다.")
    void givenPrivateMemberId_whenGettingMember_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/members/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("권한이 존재하지 않는 멤버입니다."))
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 된 유저가 private 멤버 id를 이용해 멤버를 조회하면 에러를 반환한다.")
    void givenPrivateMemberIdWithLogin_whenGettingMember_thenReturnErrorResponse() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/members/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("권한이 존재하지 않는 멤버입니다."))
        ;
    }

}