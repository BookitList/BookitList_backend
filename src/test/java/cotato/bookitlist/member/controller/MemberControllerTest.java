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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    @WithCustomMockUser
    @DisplayName("profileStatus를 변경하는 요청을 한다.")
    void givenLoginMember_whenChangingProfileStatus_thenChangeProfileStatus() throws Exception {
        //given

        //when & then
        mockMvc.perform(patch("/members/profile-status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithCustomMockUser
    @DisplayName("유저 이름을 변경한다.")
    void givenName_whenChangingName_thenChangeName() throws Exception{
        //given
        NameChangeRequest request = new NameChangeRequest("newName");

        //when & then
        mockMvc.perform(patch("/members/name")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("로그인 없이 유저 이름을 변경하면 에러를 반환한다.")
    void givenNonLogin_whenChangingName_thenReturnErrorResponse() throws Exception {
        //given
        NameChangeRequest request = new NameChangeRequest("newName");

        //when & then
        mockMvc.perform(patch("/members/name")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @DisplayName("최신 순으로 멤버를 3명 반환한다.")
    void givenMembers_whenGettingNewMembers_thenReturnNewMembers() throws Exception {
        //given

        //when & then
        mockMvc.perform(get("/members/recommend/new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberList.length()").value(3))
                .andExpect(jsonPath("$.memberList[0].memberId").value(1))
                .andExpect(jsonPath("$.memberList[1].memberId").value(2))
        ;
    }

}
