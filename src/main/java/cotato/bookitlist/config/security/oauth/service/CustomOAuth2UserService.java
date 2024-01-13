package cotato.bookitlist.config.security.oauth.service;

import cotato.bookitlist.config.security.oauth.AuthProvider;
import cotato.bookitlist.config.security.oauth.OAuth2UserInfo;
import cotato.bookitlist.config.security.oauth.OAuth2UserInfoFactory;
import cotato.bookitlist.config.security.oauth.UserPrincipal;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());
        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (member != null) {
            member = updateMember(member, oAuth2UserInfo);
        } else {
            member = registerMember(authProvider, oAuth2UserInfo);
        }
        return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    private Member registerMember(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.of(authProvider, oAuth2UserInfo);
        return memberRepository.save(member);
    }

    private Member updateMember(Member member, OAuth2UserInfo oAuth2UserInfo) {
        return member.update(oAuth2UserInfo);
    }

}
