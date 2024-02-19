package cotato.bookitlist.member.dto.response;

import cotato.bookitlist.member.domain.Member;

public record MemberRecommendResponse(
        Long memberId,
        String profileLink
) {
    public static MemberRecommendResponse of(Member member) {
        return new MemberRecommendResponse(member.getId(), member.getProfileLink());
    }
}
