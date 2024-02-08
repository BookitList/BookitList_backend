package cotato.bookitlist.post.dto.requeset;

import cotato.bookitlist.book.annotation.IsValidIsbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRegisterRequest(
        @IsValidIsbn
        String isbn13,
        @Size(max = 256)
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
