package cotato.bookitlist.member.dto.response;

import cotato.bookitlist.member.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public record MemberRecommendListResponse (
        List<MemberRecommendResponse> memberList
) {
    public static MemberRecommendListResponse of(Page<Member> page) {
        return new MemberRecommendListResponse(
                page.stream().map(MemberRecommendResponse::of).toList()
        );
    }
}
