package cotato.bookitlist.book.dto;

import cotato.bookitlist.book.domain.entity.Book;

import java.time.LocalDate;

public record BookDto(
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

    public static BookDto from(Book entity) {
        return new BookDto(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getPublisher(), entity.getPubDate(), entity.getDescription(), entity.getLink(), entity.getIsbn13(), entity.getPrice(), entity.getCover());

    }


}
