package cotato.bookitlist.book.dto.request;


import cotato.bookitlist.book.annotation.IsValidIsbn;
import jakarta.validation.constraints.NotNull;

public record BookIsbn13Request(
        @NotNull
        @IsValidIsbn
        String isbn13
) {
}
