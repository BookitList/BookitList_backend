package cotato.bookitlist.book.dto.response;

import java.util.List;

public record BookRecommendListResponse(
        List<BookRecommendResponse> bookList
) {
}
