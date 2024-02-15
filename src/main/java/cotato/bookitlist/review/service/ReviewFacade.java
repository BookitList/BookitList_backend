package cotato.bookitlist.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;

    public void deleteReview(Long reviewId, Long memberId) {
        reviewLikeService.deleteAllReviewLike(reviewId);
        reviewService.deleteReview(reviewId, memberId);
    }
}
