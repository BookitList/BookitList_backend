package cotato.bookitlist.fixture;


import cotato.bookitlist.book.domain.Book;

import java.time.LocalDate;

public class BookFixture {
    public static Book createBook(String isbn13) {
        return Book.of("title", "author", "pubisher", LocalDate.now(), "description", "link", isbn13, 10000, "cover");
    }

    public static Book createBook() {
        return createBook("isbn13");
    }
}
