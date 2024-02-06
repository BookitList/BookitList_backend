package cotato.bookitlist.book.dto.response;

import java.util.List;

public record BookApiListResponse(
        int totalResults,
        int startIndex,
        int itemsPerPage,
        List<BookApiResponse> bookApiList
) {

    public static BookApiListResponse of(int totalResults, int startIndex, int itemsPerPage, List<BookApiResponse> bookApiList) {
        return new BookApiListResponse(totalResults, startIndex, itemsPerPage, bookApiList);
    }
}
