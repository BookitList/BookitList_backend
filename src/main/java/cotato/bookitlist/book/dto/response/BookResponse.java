package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.dto.BookDto;

import java.time.LocalDate;

public record BookResponse(
        Long bookId,
        String title,
        String author,
        String publisher,
        LocalDate pubDate,
        String description,
        String link,
        String isbn13,
        Integer price,
        String cover
) {

    public static BookResponse fromBookDto(BookDto dto) {
        return new BookResponse(dto.bookId(), dto.title(), dto.author(), dto.publisher(), dto.pubDate(), dto.description(), dto.link(), dto.isbn13(), dto.price(), dto.cover());
    }

}
