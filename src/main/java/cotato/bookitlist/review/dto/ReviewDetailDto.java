package cotato.bookitlist.review.dto;

import cotato.bookitlist.review.domain.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewDetailDto(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        boolean liked,
        ReviewStatus status
) {
}
