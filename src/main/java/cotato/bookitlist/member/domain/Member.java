package cotato.bookitlist.member.domain;

import cotato.bookitlist.common.domain.BaseEntity;
import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.config.security.oauth.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE member_id = ?")
@SQLRestriction("deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String name;

    private String oauth2Id;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String profileLink;

    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus = ProfileStatus.PUBLIC;

    private boolean deleted = false;

    public Member(String email, String name, String oauth2Id, AuthProvider authProvider, String profileLink) {
        this.email = email;
        this.name = name;
        this.oauth2Id = oauth2Id;
        this.authProvider = authProvider;
        this.profileLink = profileLink;
    }

    public static Member of(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo, String profileLink) {
        return new Member(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName(), oAuth2UserInfo.getOAuth2Id(),
                authProvider, profileLink);
    }

    public Member update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }

    public String updateProfileLink(String url) {
        profileLink = url;
        return profileLink;
    }

    public void validatePubicProfile(Long memberId) {
        if (profileStatus.equals(ProfileStatus.PRIVATE) && !id.equals(memberId)) {
            throw new AccessDeniedException("권한이 존재하지 않는 멤버입니다.");
        }
    }

    public void changeProfileStatus() {
        if (profileStatus.equals(ProfileStatus.PRIVATE)) {
            profileStatus = ProfileStatus.PUBLIC;
        } else {
            profileStatus = ProfileStatus.PRIVATE;
        }
    }
}
