package cotato.bookitlist.review.repository;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBook(Book book);
}
