package cotato.bookitlist.review.dto.request;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewRegisterRequest(
        @IsValidIsbn
        String isbn13,
        @Size(max = 50)
        @NotBlank
        String content
) {
}
