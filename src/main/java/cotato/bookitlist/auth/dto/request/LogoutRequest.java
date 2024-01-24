package cotato.bookitlist.auth.dto.request;

public record LogoutRequest(
        String accessToken,
        String refreshToken
) {
}
