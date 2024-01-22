package cotato.bookitlist.auth.dto;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
