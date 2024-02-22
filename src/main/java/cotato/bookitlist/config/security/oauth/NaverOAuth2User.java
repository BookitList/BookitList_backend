package cotato.bookitlist.config.security.oauth;

import java.util.Map;

public class NaverOAuth2User extends OAuth2UserInfo {
    public NaverOAuth2User(Map<String, Object> attributes) {
        super((Map) attributes.get("response"));
    }

    public String getOAuth2Id() {
        return (String) this.attributes.get("id");
    }

    public String getEmail() {
        return (String) this.attributes.get("email");
    }

    public String getName() {
        return (String) this.attributes.get("nickname");
    }
}
