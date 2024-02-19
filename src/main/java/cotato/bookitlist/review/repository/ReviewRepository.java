package cotato.bookitlist.review.repository;

import cotato.bookitlist.review.domain.entity.Review;
import cotato.bookitlist.review.repository.querydsl.ReviewRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends ReviewRepositoryCustom, JpaRepository<Review, Long> {

    @Query("select r from Review r where r.status = 'PUBLIC'")
    Page<Review> findPublicReviewAll(Pageable pageable);

    @Query("select count(r) from Review r where r.status = 'PUBLIC' and r.book.isbn13 = :isbn13")
    int countPublicReviewByBook_Isbn13(String isbn13);

    Optional<Review> findByIdAndMemberId(Long reviewId, Long memberId);

    Page<Review> findByMemberId(Long memberId, Pageable pageable);
}
