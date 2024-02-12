package cotato.bookitlist.review.dto;

import cotato.bookitlist.review.domain.entity.Review;

public record ReviewDto(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount,
        boolean liked
) {
    public static ReviewDto from(Review entity) {
        return new ReviewDto(
                entity.getId(),
                entity.getMember().getId(),
                entity.getBook().getId(),
                entity.getContent(),
                entity.getLikeCount(),
                entity.getViewCount(),
                false
        );
    }
}
