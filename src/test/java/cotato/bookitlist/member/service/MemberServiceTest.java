package cotato.bookitlist.member.service;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.domain.ProfileStatus;
import cotato.bookitlist.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cotato.bookitlist.fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("멤버 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {


    @InjectMocks
    MemberService sut;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 id를 이용해 멤버의 ProfileStatus를 변경한다.")
    void givenMemberId_whenChangingProfileStatus_thenChangeProfileStatus() throws Exception{
        //given
        Long memberId = 1L;
        Member member = createMember(memberId);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);

        //when
        sut.changeProfileStatus(memberId);

        //then
        then(memberRepository).should().getReferenceById(memberId);
        assertThat(member.getStatus()).isEqualTo(ProfileStatus.PRIVATE);
    }

    @Test
    @DisplayName("유저의 이름을 변경한다.")
    void givenName_whenChangingName_thenChangeName() throws Exception{
        //given
        Long memberId = 1L;
        Member member = createMember(memberId);
        String name = "newName";
        given(memberRepository.getReferenceById(memberId)).willReturn(member);

        //when
        sut.changeName(name, memberId);

        //then
        then(memberRepository).should().getReferenceById(memberId);
        assertThat(member.getName()).isEqualTo(name);
    }


}
