package cotato.bookitlist.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import cotato.bookitlist.member.domain.ProfileStatus;
import cotato.bookitlist.member.dto.MemberDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberResponse(
        Long memberId,
        String email,
        String name,
        String profileLink,
        ProfileStatus profileStatus,
        boolean isMe
) {

    public static MemberResponse from(MemberDto dto) {
        if (dto.isMe()) {
            return new MemberResponse(
                    dto.memberId(),
                    dto.email(),
                    dto.name(),
                    dto.profileLink(),
                    dto.profileStatus(),
                    true
            );
        }

        return new MemberResponse(
                dto.memberId(),
                null,
                dto.name(),
                dto.profileLink(),
                dto.profileStatus(),
                false
        );
    }
}
