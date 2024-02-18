package cotato.bookitlist.fixture;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.domain.BookLike;
import cotato.bookitlist.member.domain.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class BookLikeFixture {
    public static BookLike createBookLike(Book book, Member member) {
        BookLike bookLike = BookLike.of(book, member);
        ReflectionTestUtils.setField(bookLike, "id", 1L);
        return bookLike;
    }
}
