package cotato.bookitlist.review.dto.response;

import cotato.bookitlist.review.dto.ReviewDetailDto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        boolean liked,
        boolean isMine
) {
    public static ReviewResponse from(ReviewDetailDto dto, Long memberId) {
        return new ReviewResponse(
                dto.reviewId(),
                dto.memberId(),
                dto.bookId(),
                dto.content(),
                dto.likeCount(),
                dto.viewCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.liked(),
                dto.memberId().equals(memberId)
        );
    }
}
