package cotato.bookitlist.member.dto;

public record ProfileResponse(
        String url
) {

    public static ProfileResponse of(String url) {
        return new ProfileResponse(url);
    }
}
