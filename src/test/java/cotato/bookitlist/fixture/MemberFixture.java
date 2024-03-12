package cotato.bookitlist.fixture;

import cotato.bookitlist.security.oauth.AuthProvider;
import cotato.bookitlist.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static Member createMember(Long memberId) {
        Member member = new Member("email", "name", "oauth2Id", AuthProvider.KAKAO, "profile");
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }

    public static Member createMember() {
        return createMember(1L);
    }
}
