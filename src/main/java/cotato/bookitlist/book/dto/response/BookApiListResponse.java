package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.dto.BookApiDto;

import java.util.List;

public record BookApiListResponse(
        int totalResults,
        int startIndex,
        int itemsPerPage,
        List<BookApiDto> bookApiList
) {

    public static BookApiListResponse of(int totalResults, int startIndex, int itemsPerPage, List<BookApiDto> bookApiDto) {
        return new BookApiListResponse(totalResults, startIndex, itemsPerPage, bookApiDto);
    }
}
