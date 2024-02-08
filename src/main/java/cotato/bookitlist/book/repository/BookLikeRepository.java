package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.entity.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepository extends JpaRepository<BookLike, Long> {
}
