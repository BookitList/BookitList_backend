package cotato.bookitlist.config.security.oauth.handler;

import cotato.bookitlist.config.security.jwt.JwtTokenProvider;
import cotato.bookitlist.config.security.oauth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${oauth.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtTokenProvider jwtTokenProvider;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
        } else {
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            Long id = userDetails.getId();
            String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
            String accessToken = this.jwtTokenProvider.generateAccessToken(id, authorities);
            String uri = UriComponentsBuilder.fromUriString(this.redirectUri).queryParam("accessToken", accessToken).toUriString();
            this.getRedirectStrategy().sendRedirect(request, response, uri);
        }
    }

    public OAuth2AuthenticationSuccessHandler(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
}
