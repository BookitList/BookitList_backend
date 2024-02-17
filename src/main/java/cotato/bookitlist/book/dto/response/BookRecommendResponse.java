package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.dto.BookDto;

public record BookRecommendResponse(
        String title,
        String author,
        String description,
        String isbn13,
        String cover
) {
    public static BookRecommendResponse from(BookDto dto) {
        return new BookRecommendResponse(
                dto.title(),
                dto.author(),
                dto.description(),
                dto.isbn13(),
                dto.cover()
        );
    }
}
