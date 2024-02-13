package cotato.bookitlist.member.dto;

import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.domain.ProfileStatus;

public record MemberDto(
        Long memberId,
        String email,
        String name,
        String profileLink,
        ProfileStatus profileStatus,
        boolean isMe
) {

    public static MemberDto from(Member entity, Long memberId) {
        return new MemberDto(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getProfileLink(),
                entity.getProfileStatus(),
                entity.getId().equals(memberId)
        );
    }

}
