package cotato.bookitlist.review.repository;

import cotato.bookitlist.review.domain.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);
    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);

    @Modifying
    @Query(value = "delete from ReviewLike r where r.id=:reviewId")
    void deleteAllByReviewId(Long reviewId);
}
