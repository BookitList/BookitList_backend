package cotato.bookitlist.book.repository;

import cotato.bookitlist.book.domain.Book;
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
    @Query("select b from Book b where LOWER(b.title) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(b.author) like LOWER(CONCAT('%', :keyword, '%')) or LOWER(b.description) like LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select b from BookLike bl join bl.book b where bl.member.id = :memberId order by bl.createdAt desc")
    Page<Book> findLikeBookByMemberId(Long memberId, Pageable pageable);

    @Query(value = "select * from book b order by RAND() LIMIT :count", nativeQuery = true)
    List<Book> findBooksByRandom(int count);
}
