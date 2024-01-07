package cotato.bookitlist.book.dto;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.dto.response.BookApiResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record BookApiDto(
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

    public Book toEntity() {
        return Book.of(title, author, publisher, pubDate, description, link, isbn13, price, cover);
    }

    public BookApiResponse toBookApiResponse() {
        return new BookApiResponse(1, 1, 1, List.of(this));
    }

    public static BookApiDto from(
            String title,
            String author,
            String publisher,
            String pubDateString,
            String description,
            String link,
            String isbn13,
            Integer price,
            String coverString
    ) {

        String cover = coverString.replace("cover", "cover500");

        LocalDate pubDate = null;
        if (!pubDateString.isEmpty()) {
            pubDate = LocalDate.parse(pubDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return new BookApiDto(title, author, publisher, pubDate, description, link, isbn13, price, cover);
    }


}
