package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.review.domain.ReviewStatus;
import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.dto.ReviewDto;

public record ReviewResponse(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount,
        boolean liked,
        boolean isMine,
        ReviewStatus status
) {
    public static ReviewResponse from(Review entity, Long memberId) {
        return new ReviewResponse(
                entity.getId(),
                entity.getMember().getId(),
                entity.getBook().getId(),
                entity.getContent(),
                entity.getLikeCount(),
                entity.getViewCount(),
                false,
                entity.getMember().getId().equals(memberId),
                entity.getStatus()
        );
    }

    public static ReviewResponse from(ReviewDto dto, Long memberId) {
        return new ReviewResponse(
                dto.reviewId(),
                dto.memberId(),
                dto.bookId(),
                dto.content(),
                dto.likeCount(),
                dto.viewCount(),
                dto.liked(),
                dto.memberId().equals(memberId),
                dto.reviewStatus()
        );
    }
}
