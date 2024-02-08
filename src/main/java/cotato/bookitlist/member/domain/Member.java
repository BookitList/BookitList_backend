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

    private int followCount = 0;

    private boolean deleted = false;

    public Member(String email, String name, String oauth2Id, AuthProvider authProvider) {
        this.email = email;
        this.name = name;
        this.oauth2Id = oauth2Id;
        this.authProvider = authProvider;
    }

    public static Member of(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        return new Member(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName(), oAuth2UserInfo.getOAuth2Id(),
                authProvider);
    }

    public Member update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();

        return this;
    }

    public String updateProfileLine(String url) {
        profileLink = url;
        return profileLink;
    }
}
