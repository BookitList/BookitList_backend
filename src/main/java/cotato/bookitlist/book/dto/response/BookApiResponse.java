package cotato.bookitlist.book.dto.response;

import cotato.bookitlist.book.dto.BookApiDto;

import java.util.List;

public record BookApiResponse(
        int totalResults,
        int startIndex,
        int itemsPerPage,
        List<BookApiDto> bookApiDtoList
) {

    public static BookApiResponse of(int totalResults, int startIndex, int itemsPerPage, List<BookApiDto> bookApiDto) {
        return new BookApiResponse(totalResults, startIndex, itemsPerPage, bookApiDto);
    }
}
