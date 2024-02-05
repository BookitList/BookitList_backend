package cotato.bookitlist.review.dto.response;

public record ReviewCountResponse(
        int count
) {
    public static ReviewCountResponse of(int count) {
        return new ReviewCountResponse(count);
    }
}
