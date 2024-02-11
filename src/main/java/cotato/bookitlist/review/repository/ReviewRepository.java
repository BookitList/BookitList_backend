package cotato.bookitlist.review.repository;

import cotato.bookitlist.review.domain.Review;
import cotato.bookitlist.review.repository.querydsl.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends ReviewRepositoryCustom, JpaRepository<Review, Long> {

    int countByBook_Isbn13(String isbn13);
}
