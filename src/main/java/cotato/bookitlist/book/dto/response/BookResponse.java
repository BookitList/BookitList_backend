package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.dto.BookDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record BookResponse(
        int totalResults,
        int startIndex,
        int itemsPerPage,
        List<BookDto> bookList
) {

    public static BookResponse of(int totalResults, int startIndex, int itemsPerPage, List<BookDto> bookDtoList) {
        return new BookResponse(totalResults, startIndex, itemsPerPage, bookDtoList);
    }

    public static BookResponse from(Page<Book> bookPage) {
        return new BookResponse(
                (int) bookPage.getTotalElements(),
                bookPage.getNumber(),
                bookPage.getNumberOfElements(),
                bookPage
                        .stream()
                        .map(BookDto::from)
                        .toList()
        );
    }
}
