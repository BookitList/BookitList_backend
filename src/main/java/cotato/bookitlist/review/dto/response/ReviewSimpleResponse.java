package cotato.bookitlist.review.dto.response;

public record ReviewSimpleResponse(
        Long reviewId,
        String content,
        String name
) {
}
