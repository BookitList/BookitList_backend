package cotato.bookitlist.security.jwt;

import cotato.bookitlist.security.jwt.dto.AccessTokenInfo;
import cotato.bookitlist.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Jws<Claims> getJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties
                .getSecretKey()
                .getBytes(StandardCharsets.UTF_8));
    }

    public Long getExpiration(String token) {
        Date expiration = getJws(token).getBody().getExpiration();
        return expiration.getTime() - new Date().getTime();
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
        Date accessTokenExpiresIn = new Date(issuedAt.getTime() + getAccessTokenTtlMilliSecond());

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, role);
    }

    public String generateRefreshToken(Long id) {
        Date issuedAt = new Date();
        Date refreshTokenExpiresIn = new Date(issuedAt.getTime() + getRefreshTokenTtlMilliSecond());

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
            throw new UnsupportedJwtException("올바르지 않은 Access Token입니다.");
        }
    }

    public Long parseRefreshToken(String token) {
        if (isRefreshToken(token)) {
            Claims claims = getJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } else {
            throw new UnsupportedJwtException("올바르지 않은 Refresh Token입니다.");
        }
    }

    public Long getAccessTokenTtlMilliSecond() {
        return jwtProperties.getAccessExp();
    }

    public Long getRefreshTokenTtlMilliSecond() {
        return jwtProperties.getRefreshExp();
    }

}
