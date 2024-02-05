package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.post.dto.response.PostCountResponse;

public record ReviewCountResponse(
        int count
) {
    public static ReviewCountResponse of(int count) {
        return new ReviewCountResponse(count);
    }
}
