package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.domain.Book;
import cotato.bookitlist.book.dto.BookDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record BookListResponse(
        int totalResults,
        int totalPages,
        int startIndex,
        int itemsPerPage,
        List<BookDto> bookList
) {

    public static BookListResponse from(Page<Book> bookPage) {
        return new BookListResponse(
                (int) bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.getNumber(),
                bookPage.getNumberOfElements(),
                bookPage
                        .stream()
                        .map(BookDto::from)
                        .toList()
        );
    }
}
