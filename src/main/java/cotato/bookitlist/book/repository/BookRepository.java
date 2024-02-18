package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn13(String isbn13);

    // TODO: 단순 like query로 한영에 따라 결과가 다르게 나온다. 이를 해결해야함!!
    @Query("select b from Book b where lower(b.title) like lower(concat('%', :keyword, '%')) or lower(b.author) like lower(concat('%', :keyword, '%')) or lower(b.description) like lower(concat('%', :keyword, '%'))")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select b from BookLike l join l.book b join l.member m where m.id = :memberId")
    Page<Book> findLikeBookByMemberId(Long memberId, Pageable pageable);
}
