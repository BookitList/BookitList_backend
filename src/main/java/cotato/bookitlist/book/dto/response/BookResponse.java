package cotato.bookitlist.book.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import cotato.bookitlist.book.dto.BookDto;

import java.time.LocalDate;

public record BookResponse(
        String title,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate pubDate,
        String description,
        String link,
        String isbn13,
        Integer price,
        String cover
) {

    public static BookResponse fromBookDto(BookDto dto) {
        return new BookResponse(
                dto.title(),
                dto.author(),
                dto.publisher(),
                dto.pubDate(),
                dto.description(),
                dto.link(),
                dto.isbn13(),
                dto.price(), dto.cover()
        );
    }

}
