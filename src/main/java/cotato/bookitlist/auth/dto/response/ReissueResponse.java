package cotato.bookitlist.auth.dto.response;

public record ReissueResponse(
        String accessToken,
        String refreshToken
) {

    public static ReissueResponse from(String accessToken, String refreshToken) {
        return new ReissueResponse(accessToken, refreshToken);
    }
}
