package cotato.bookitlist.config.security;

import cotato.bookitlist.config.security.jwt.JwtAuthenticationFilter;
import cotato.bookitlist.config.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import cotato.bookitlist.config.security.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String[] WHITE_LIST = {
            "/health_check",
            "/swagger-ui/**",
            "/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers("/posts/likes").authenticated()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler))
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));
        return http.build();
    }

}
