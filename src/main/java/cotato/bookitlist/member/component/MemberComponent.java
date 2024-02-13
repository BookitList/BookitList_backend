package cotato.bookitlist.member.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MemberComponent {
    @Value("${profile.default.url}")
    private String profileDefaultUrl;

    @Bean
    public String getDefaultProfileUrl() {
        return profileDefaultUrl;
    }
}
