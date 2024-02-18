package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookLikeRepository extends JpaRepository<BookLike, Long> {

    boolean existsByBook_Isbn13AndMemberId(String isbn13, Long memberId);

    Optional<BookLike> findByBook_Isbn13AndMemberId(String isbn13, Long memberId);
}
