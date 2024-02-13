package cotato.bookitlist.member.dto.response;

public record ProfileResponse(
        String url
) {

    public static ProfileResponse of(String url) {
        return new ProfileResponse(url);
    }
}
