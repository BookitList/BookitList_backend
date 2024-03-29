package cotato.bookitlist.review.repository.querydsl;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.review.dto.ReviewDetailDto;
import cotato.bookitlist.review.dto.ReviewDto;
import cotato.bookitlist.review.dto.response.ReviewSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewRepositoryCustom {
    Page<ReviewDto> findPublicReviewWithLikedByIsbn13(String isbn13, Long memberId, Long loginMemberId, Pageable pageable);

    Optional<ReviewDetailDto> findReviewDetailByReviewId(Long reviewId, Long memberId);

    Page<ReviewDto> findLikeReviewByMemberId(Long memberId, Pageable pageable);

    Optional<ReviewSimpleResponse> findBestReview(Book book);
}
