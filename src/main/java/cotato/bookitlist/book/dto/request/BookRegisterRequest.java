package cotato.bookitlist.book.dto.request;


import cotato.bookitlist.book.annotation.IsValidIsbn;

public record BookRegisterRequest(
        @IsValidIsbn
        String isbn13
) {
}
