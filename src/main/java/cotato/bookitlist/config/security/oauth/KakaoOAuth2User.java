package cotato.bookitlist.config.security.oauth;

import java.util.Map;

public class KakaoOAuth2User extends OAuth2UserInfo {
    private final Long id;

    public KakaoOAuth2User(Map<String, Object> attributes) {
        super((Map) attributes.get("kakao_account"));
        this.id = (Long) attributes.get("id");
    }

    @Override
    public String getOAuth2Id() {
        return this.id.toString();
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map) this.attributes.get("profile")).get("nickname");
    }
}
