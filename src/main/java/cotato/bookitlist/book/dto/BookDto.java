package cotato.bookitlist.book.dto;


import cotato.bookitlist.book.domain.Book;

import java.time.LocalDate;

public record BookDto(
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

    public static BookDto from(Book entity) {
        return new BookDto(
                entity.getTitle(),
                entity.getAuthor(),
                entity.getPublisher(),
                entity.getPubDate(),
                entity.getDescription(),
                entity.getLink(),
                entity.getIsbn13(),
                entity.getPrice(),
                entity.getCover()
        );
    }

    public static BookDto from(BookApiDto bookApiDto) {
        return new BookDto(
                bookApiDto.title(),
                bookApiDto.author(),
                bookApiDto.publisher(),
                bookApiDto.pubDate(),
                bookApiDto.description(),
                bookApiDto.link(),
                bookApiDto.isbn13(),
                bookApiDto.price(),
                bookApiDto.cover()
        );
    }
}
