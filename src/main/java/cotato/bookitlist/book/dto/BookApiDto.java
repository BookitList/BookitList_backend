package cotato.bookitlist.book.dto;


import cotato.bookitlist.book.domain.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public static BookApiDto of(
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

        String cover = coverString.replace("coversum", "cover500");

        LocalDate pubDate = null;
        if (!pubDateString.isEmpty()) {
            pubDate = LocalDate.parse(pubDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return new BookApiDto(title, author, publisher, pubDate, description, link, isbn13, price, cover);
    }

    public static BookApiDto of(BookDto dto) {
        return new BookApiDto(dto.title(), dto.author(), dto.publisher(), dto.pubDate(), dto.description(), dto.link(), dto.isbn13(), dto.price(), dto.cover());
    }


}
