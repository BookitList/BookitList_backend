package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.review.dto.ReviewDto;

public record ReviewResponse(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount
) {
    public static ReviewResponse from(ReviewDto reviewDto) {
        return new ReviewResponse(
                reviewDto.reviewId(),
                reviewDto.memberId(),
                reviewDto.bookId(),
                reviewDto.content(),
                reviewDto.likeCount(),
                reviewDto.viewCount()
        );
    }
}
