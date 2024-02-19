package cotato.bookitlist.book.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import cotato.bookitlist.book.dto.BookApiDto;

import java.time.LocalDate;

public record BookApiResponse(
        String title,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy.MM.dd")
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
