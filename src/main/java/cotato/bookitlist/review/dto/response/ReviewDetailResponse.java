package cotato.bookitlist.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import cotato.bookitlist.review.domain.ReviewStatus;
import cotato.bookitlist.review.dto.ReviewDetailDto;

import java.time.LocalDateTime;

public record ReviewDetailResponse(
        Long reviewId,
        Long memberId,
        Long bookId,
        String content,
        int likeCount,
        int viewCount,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDateTime modifiedAt,
        boolean liked,
        boolean isMine,
        ReviewStatus status
) {
    public static ReviewDetailResponse from(ReviewDetailDto dto, Long memberId) {
        return new ReviewDetailResponse(
                dto.reviewId(),
                dto.memberId(),
                dto.bookId(),
                dto.content(),
                dto.likeCount(),
                dto.viewCount(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.liked(),
                dto.memberId().equals(memberId),
                dto.reviewStatus()
        );
    }
}
