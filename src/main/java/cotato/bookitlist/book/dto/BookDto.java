package cotato.bookitlist.book.dto;

import cotato.bookitlist.book.domain.entity.Book;
import cotato.bookitlist.book.dto.response.BookListResponse;

import java.time.LocalDate;
import java.util.List;

public record BookDto(
        Long id,
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

    public BookListResponse toBookResponse() {
        return new BookListResponse(1, 0, 1, List.of(this));
    }

    public static BookDto from(Book entity) {
        return new BookDto(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getPublisher(), entity.getPubDate(), entity.getDescription(), entity.getLink(), entity.getIsbn13(), entity.getPrice(), entity.getCover());

    }


}
