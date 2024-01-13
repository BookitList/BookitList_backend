package cotato.bookitlist.config.security.jwt;

import cotato.bookitlist.config.security.jwt.dto.AccessTokenInfo;
import cotato.bookitlist.config.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Jws<Claims> getJws(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private String buildAccessToken(Long id, Date issuedAt, Date accessTokenExpiresIn, String role) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer("bookitlist")
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim("type", "ACCESS_TOKEN")
                .claim("role", role)
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    private String buildRefreshToken(Long id, Date issuedAt, Date accessTokenExpiresIn) {
        Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer("bookitlist")
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim("type", "REFRESH_TOKEN")
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    public String generateAccessToken(Long id, String role) {
        Date issuedAt = new Date();
        Date accessTokenExpiresIn = new Date(issuedAt.getTime() + getAccessTokenTTlSecond() * 1000);

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, role);
    }

    public String generateRefreshToken(Long id) {
        Date issuedAt = new Date();
        Date refreshTokenExpiresIn = new Date(issuedAt.getTime() + getRefreshTokenTTlSecond() * 1000);

        return buildRefreshToken(id, issuedAt, refreshTokenExpiresIn);
    }

    public boolean isAccessToken(String token) {
        return getJws(token).getBody().get("type").equals("ACCESS_TOKEN");
    }

    public boolean isRefreshToken(String token) {
        return getJws(token).getBody().get("type").equals("REFRESH_TOKEN");
    }

    public AccessTokenInfo parseAccessToken(String token) {
        if (isAccessToken(token)) {
            Claims claims = getJws(token).getBody();
            return AccessTokenInfo.of(
                    Long.parseLong(claims.getSubject()),
                    (String) claims.get("role")
            );
        } else {
            throw new RuntimeException();
        }
    }

    public Long parseRefreshToken(String token) {
        try {
            if (isRefreshToken(token)) {
                Claims claims = getJws(token).getBody();
                return Long.parseLong(claims.getSubject());
            }
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException();
        }

        throw new RuntimeException();
    }

    public Long getRefreshTokenTTlSecond() {
        return jwtProperties.getRefreshExp();
    }

    public Long getAccessTokenTTlSecond() {
        return jwtProperties.getAccessExp();
    }

}
