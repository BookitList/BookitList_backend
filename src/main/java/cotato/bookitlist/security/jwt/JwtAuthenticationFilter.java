package cotato.bookitlist.security.jwt;

import cotato.bookitlist.auth.service.AuthService;
import cotato.bookitlist.security.jwt.dto.AccessTokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null && !authService.isBlocked(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        if (accessTokenCookie != null) {
            return accessTokenCookie.getValue();
        } else {
            String rawHeader = request.getHeader("Authorization");
            String bearer = "Bearer ";
            return rawHeader != null && rawHeader.length() > bearer.length() && rawHeader.startsWith(bearer) ? rawHeader.substring(bearer.length()) : null;
        }
    }

    public Authentication getAuthentication(String token) {
        AccessTokenInfo accessTokenInfo = jwtTokenProvider.parseAccessToken(token);
        authService.validateRegisteredMember(accessTokenInfo.userId());
        UserDetails userDetails = new AuthDetails(accessTokenInfo.userId().toString(), accessTokenInfo.role());
        return new UsernamePasswordAuthenticationToken(userDetails, "user", userDetails.getAuthorities());
    }
}
