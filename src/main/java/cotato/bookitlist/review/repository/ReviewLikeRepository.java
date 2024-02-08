package cotato.bookitlist.review.repository;

import cotato.bookitlist.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);
    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);
}
