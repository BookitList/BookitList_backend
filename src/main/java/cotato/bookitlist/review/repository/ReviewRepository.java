package cotato.bookitlist.review.repository;

import cotato.bookitlist.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBook_Isbn13(String isbn13, Pageable pageable);
    int countByBook_Isbn13(String isbn13);
}
