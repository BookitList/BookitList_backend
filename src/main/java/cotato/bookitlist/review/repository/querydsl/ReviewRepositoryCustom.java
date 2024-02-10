package cotato.bookitlist.review.repository.querydsl;

import cotato.bookitlist.review.dto.ReviewDto;

import java.util.Optional;

public interface ReviewRepositoryCustom {

    Optional<ReviewDto> findReviewByReviewId(Long reviewId, Long memberId);
}
