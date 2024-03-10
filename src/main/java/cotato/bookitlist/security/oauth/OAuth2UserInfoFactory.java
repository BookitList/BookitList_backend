package cotato.bookitlist.security.oauth;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        return switch (authProvider) {
            case NAVER -> new NaverOAuth2User(attributes);
            case KAKAO -> new KakaoOAuth2User(attributes);
        };
    }
}
