package cotato.bookitlist.review.repository.querydsl;

import cotato.bookitlist.review.dto.ReviewDetailDto;
import cotato.bookitlist.review.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewRepositoryCustom {
    Page<ReviewDto> findReviewWithLikedByIsbn13(String isbn13, Long memberId, Pageable pageable);
    Optional<ReviewDetailDto> findReviewDetailByReviewId(Long reviewId, Long memberId);
}
