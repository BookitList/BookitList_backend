package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn13(String isbn13);

    // TODO: 단순 like query로 한영에 따라 결과가 다르게 나온다. 이를 해결해야함!!
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM Book b ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Book> findBooksByRandom(int count);
}
