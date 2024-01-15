package cotato.bookitlist.config.security.jwt.dto;

public record AccessTokenInfo(
        Long userId,
        String role
) {
    public static AccessTokenInfo of(Long userId, String role) {
        return new AccessTokenInfo(userId, role);
    }

}