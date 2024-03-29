package cotato.bookitlist.security.oauth.service;

import cotato.bookitlist.security.oauth.AuthProvider;
import cotato.bookitlist.security.oauth.OAuth2UserInfo;
import cotato.bookitlist.security.oauth.OAuth2UserInfoFactory;
import cotato.bookitlist.security.oauth.UserPrincipal;
import cotato.bookitlist.member.component.MemberComponent;
import cotato.bookitlist.member.domain.Member;
import cotato.bookitlist.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final MemberComponent memberComponent;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());
        Member member = memberRepository.findByOauth2IdAndAuthProvider(oAuth2UserInfo.getOAuth2Id(), authProvider);
        if (member != null) {
            member = updateMember(member, oAuth2UserInfo);
        } else {
            member = registerMember(authProvider, oAuth2UserInfo);
        }
        return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    private Member registerMember(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.of(authProvider, oAuth2UserInfo, memberComponent.getDefaultProfileUrl());
        return memberRepository.save(member);
    }

    private Member updateMember(Member member, OAuth2UserInfo oAuth2UserInfo) {
        return member.update(oAuth2UserInfo);
    }

}
