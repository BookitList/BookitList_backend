package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.dto.BookApiDto;

import java.time.LocalDate;

public record BookApiResponse(
        String title,
        String author,
        String publisher,
        LocalDate pubDate,
        String description,
        String link,
        String isbn13,
        Integer price,
        String cover,
        boolean isBook
) {

    public static BookApiResponse from(BookApiDto dto) {
        return new BookApiResponse(
                dto.title(),
                dto.author(),
                dto.publisher(),
                dto.pubDate(),
                dto.description(),
                dto.link(),
                dto.isbn13(),
                dto.price(),
                dto.cover(),
                !dto.isbn13().isBlank());
    }
}
