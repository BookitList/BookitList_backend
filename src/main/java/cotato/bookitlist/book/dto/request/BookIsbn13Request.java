package cotato.bookitlist.book.dto.request;


import cotato.bookitlist.book.annotation.IsValidIsbn;

public record BookIsbn13Request(
        @IsValidIsbn
        String isbn13
) {
}
